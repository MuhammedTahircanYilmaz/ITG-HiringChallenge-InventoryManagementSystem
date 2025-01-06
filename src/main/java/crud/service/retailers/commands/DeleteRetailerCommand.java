package crud.service.retailers.commands;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.base.ServiceResult;
import crud.dtos.bills.requests.DeleteBillCommandDto;
import crud.dtos.retailers.requests.DeleteRetailerCommandDto;
import crud.exception.AuthenticationException;
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

public class DeleteRetailerCommand extends AbstractCommand {

    private final RetailerRepository repository;
    private final RetailerValidator validator;
    private final AuthService authService;
    private final RetailerMapper mapper;
    private String page = "";

    public DeleteRetailerCommand(RetailerRepository repository , RetailerValidator validator, AuthService authService, RetailerMapper mapper) {
        this.repository = repository;
        this.authService = authService;
        this.mapper = mapper;
        this.validator = validator;
    }

    @Override
    public ServiceResult execute(HttpServletRequest request) {
        try{
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            UUID userId = authService.getUserId(token);
            Retailer retailer = repository.findById(userId);

            if(!authService.isAllowed(token,retailer.getId())){
                throw new AuthenticationException("The user is not allowed to delete this retailer");
            }

            DeleteRetailerCommandDto dto = mapper.mapDeleteRequestDto(request, userId.toString());

            retailer.setDeletedBy(dto.getDeletedBy());
            retailer.setDeletedAt(dto.getDeletedAt());
            validator.validateDeleteRequest(request);

            repository.delete(retailer);
            addSuccessMessage(request,DELETE_SUCCESS_MESSAGE);

        } catch (DAOException | MappingException ex){
            Logger.error(this.getClass().getName(), ex.getMessage());
            page = "";
            setException(request, ex);
        }
        return createView(page);
    }
}
