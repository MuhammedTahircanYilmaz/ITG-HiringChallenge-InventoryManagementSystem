package crud.service.retailers.queries;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.dtos.retailers.responses.RetailerResponseDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.RetailerMapper;
import crud.model.entities.Retailer;
import crud.repository.RetailerRepository;
import crud.service.validation.RetailerValidator;
import crud.util.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class GetByIdRetailerQuery extends AbstractCommand {

    private final RetailerRepository repository;
    private final RetailerValidator validator;
    private final AuthService authService ;
    private final RetailerMapper mapper;
    private String page = PAGE_RETAILER_LIST;

    public GetByIdRetailerQuery(RetailerRepository repository , RetailerValidator validator, AuthService authService, RetailerMapper mapper) {
        this.repository = repository;
        this.authService = authService;
        this.mapper = mapper;
        this.validator = validator;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try{
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            validator.validateGetByIdRequest(request);

            Retailer retailer = repository.findById(UUID.fromString(request.getParameter("RetailerId")));
            RetailerResponseDto dto = mapper.mapEntityToEntityResponseDto(retailer);

            setEntity(request, dto);
        } catch (DAOException | MappingException ex){

            Logger.error(this.getClass().getName(), ex.getMessage());
            page = PAGE_RETAILER_LIST;

            setException(request, ex);
        }
        return page;
    }
}
