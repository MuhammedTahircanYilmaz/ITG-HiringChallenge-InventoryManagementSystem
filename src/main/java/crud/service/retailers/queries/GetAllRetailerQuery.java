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
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;


public class GetAllRetailerQuery extends AbstractCommand {
    public GetAllRetailerQuery(RetailerRepositoryImpl repository, AuthService authService, RetailerMapper mapper) {
        this.repository = repository;
        this.authService = authService;
        this.mapper = mapper;
    }

    private RetailerRepositoryImpl repository;
    private final AuthService authService;
    private final RetailerMapper mapper;
    private String page = "";

    @Override
    public ServiceResult execute(HttpServletRequest request) {
        try{
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);


            ArrayList<Retailer> retailers = repository.findAll();
            ArrayList<RetailerResponseDto> response = new ArrayList<>();
            for(Retailer retailer : retailers){
                RetailerResponseDto dto = mapper.mapEntityToEntityResponseDto(retailer);
                response.add(dto);
            }

            return createJsonResponse(response);

        } catch (DAOException | MappingException ex){
            Logger.error(this.getClass().getName(), ex.getMessage());
            setException(request, ex);

        }
        return createView(page);
    }
}
