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
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;

public class GetByNameRetailerQuery extends AbstractCommand {

    private final RetailerRepository repository;
    private final RetailerValidator validator;
    private final AuthService authService ;
    private final RetailerMapper mapper;
    private String page = PAGE_RETAILER_FORM;

    public GetByNameRetailerQuery(RetailerRepository repository, RetailerValidator validator, AuthService authService, RetailerMapper mapper) {
        this.repository = repository;
        this.validator = validator;
        this.authService = authService;
        this.mapper = mapper;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try {

            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            ArrayList<Retailer> retailers = repository.findAllByName(request.getParameter("retailerName"));
            ArrayList<RetailerResponseDto> response = new ArrayList<>();

            for(Retailer retailer : retailers){
                RetailerResponseDto dto = mapper.mapEntityToEntityResponseDto(retailer);
                response.add(dto);
            }

            setEntities(request, response,"");

        } catch (DAOException | IllegalArgumentException | MappingException ex) {

            Logger.error(this.getClass().getName(), ex.getMessage());

            page = PAGE_RETAILER_LIST;

            setException(request, ex);
        }
        return page;
    }
}