package crud.service.retailers.commands;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.base.ServiceResult;
import crud.dtos.retailers.requests.UpdateRetailerCommandDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.RetailerMapper;
import crud.model.entities.Retailer;
import crud.repository.retailer.RetailerRepositoryImpl;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class UpdateRetailerCommand extends AbstractCommand {

    public UpdateRetailerCommand(RetailerRepositoryImpl repository, RetailerMapper mapper, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
        this.mapper = mapper;
    }

    private String page = PROFILE;
    private RetailerRepositoryImpl repository;
    private RetailerMapper mapper;
    private final AuthService authService;

    @Override
    public ServiceResult execute(HttpServletRequest request) {

        try{

            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            UUID userId = authService.getUserId(token);
            UUID retailerId = UUID.fromString(request.getParameter("retailerId"));

            mapper.mapUpdateRequestDto(request, userId.toString());

            Retailer retailer = repository.findById(retailerId);

            authService.isAllowed(token,retailer.getId());

            UpdateRetailerCommandDto dto = mapper.mapUpdateRequestDto(request, userId.toString());

            retailer.setImageLocation(dto.getImageLocation());
            retailer.setName(dto.getName());
            retailer.setUpdatedAt(dto.getUpdatedAt());
            retailer.setUpdatedBy(dto.getUpdatedBy());

            repository.update(retailer);

            addSuccessMessage(request,UPDATE_SUCCESS_MESSAGE);

        } catch(DAOException | MappingException ex){

            Logger.error(this.getClass().getName(), ex.getMessage());
            page = PROFILE;

            setException(request, ex);
        }
        return createView(page);
    }

}
