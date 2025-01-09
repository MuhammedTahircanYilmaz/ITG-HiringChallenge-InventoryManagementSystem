package crud.service.retailers.commands;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.base.ServiceResult;
import crud.exception.BusinessException;
import crud.exception.DAOException;
import crud.mapper.RetailerMapper;
import crud.model.entities.Retailer;
import crud.repository.RetailerRepository;
import crud.service.validation.RetailerValidator;
import crud.util.Logger;
import crud.util.PasswordUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class ChangePasswordRetailerCommand extends AbstractCommand {

    private final RetailerRepository repository;
    private final RetailerValidator validator;
    private final AuthService authService;
    private String page = PROFILE;

    public ChangePasswordRetailerCommand(RetailerRepository repository , RetailerValidator validator, AuthService authService) {
        this.repository = repository;
        this.validator = validator;
        this.authService = authService;
    }

    @Override
    public ServiceResult execute(HttpServletRequest request) {
        try{
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            UUID userId = authService.getUserId(token);

            validator.validateChangePasswordRequest(request);

            Retailer retailer = repository.findById(userId);

            String currentPassword = request.getParameter("currentPassword");
            PasswordUtils.verifyPassword(currentPassword, retailer.getPassword());

            String newPassword = request.getParameter("newPassword");
            retailer.setPassword(PasswordUtils.hashPassword(newPassword));

            repository.update(retailer);
        } catch (DAOException | BusinessException ex){
            Logger.error(this.getClass().getName() ,ex.getMessage());

            page = PROFILE;
            setException(request, ex);
        }
        return createView( page);
    }
}
