package crud.service.suppliers.commands;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.base.ServiceResult;
import crud.exception.BusinessException;
import crud.exception.DAOException;
import crud.model.entities.Supplier;
import crud.model.entities.Supplier;
import crud.repository.SupplierRepository;
import crud.service.validation.SupplierValidator;
import crud.util.Logger;
import crud.util.PasswordUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class ChangePasswordSupplierCommand extends AbstractCommand {

    private final SupplierRepository repository;
    private final SupplierValidator validator;
    private final AuthService authService;
    private String page = PROFILE;

    public ChangePasswordSupplierCommand(SupplierRepository repository, SupplierValidator validator, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
        this.validator = validator;
    }

    @Override
    public ServiceResult execute(HttpServletRequest request) {
        try{
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            UUID userId = authService.getUserId(token);

            validator.validateChangePasswordRequest(request);

            Supplier Supplier = repository.findById(userId);

            String currentPassword = request.getParameter("currentPassword");
            PasswordUtils.verifyPassword(currentPassword, Supplier.getPassword());

            String newPassword = request.getParameter("newPassword");
            Supplier.setPassword(PasswordUtils.hashPassword(newPassword));

            repository.update(Supplier);
        } catch (DAOException | BusinessException ex){
            Logger.error(this.getClass().getName() ,ex.getMessage());

            page = PROFILE;
            setException(request, ex);
        }
        return createView(page);
    }
}
