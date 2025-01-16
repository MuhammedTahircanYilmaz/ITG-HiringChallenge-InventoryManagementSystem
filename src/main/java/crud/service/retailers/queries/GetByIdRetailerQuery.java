package crud.service.retailers.queries;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.base.ServiceResult;
import crud.dtos.retailers.responses.RetailerResponseDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.RetailerMapper;
import crud.model.entities.Retailer;
import crud.repository.retailer.RetailerRepositoryImpl;
import crud.service.retailers.rules.RetailerBusinessRules;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class GetByIdRetailerQuery extends AbstractCommand {

    private final RetailerRepositoryImpl repository;
    private final RetailerBusinessRules BusinessRules;
    private final AuthService authService ;
    private final RetailerMapper mapper;
    private String page = PROFILE;

    public GetByIdRetailerQuery(RetailerRepositoryImpl repository , RetailerBusinessRules BusinessRules, AuthService authService, RetailerMapper mapper) {
        this.repository = repository;
        this.authService = authService;
        this.mapper = mapper;
        this.BusinessRules = BusinessRules;
    }

    @Override
    public ServiceResult execute(HttpServletRequest request) {
        try{
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            BusinessRules.entityExists(UUID.fromString(request.getParameter("id")));

            Retailer retailer = repository.findById(UUID.fromString(request.getParameter("retailerId")));
            RetailerResponseDto dto = mapper.mapEntityToEntityResponseDto(retailer);

            return createJsonResponse(dto);
        } catch (DAOException | MappingException ex){

            Logger.error(this.getClass().getName(), ex.getMessage());
            page = PROFILE;

            setException(request, ex);
        }
        return createView(page);
    }
}
