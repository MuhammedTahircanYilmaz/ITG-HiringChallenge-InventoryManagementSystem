package crud.service.images.commands;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.base.ServiceResult;
import crud.exception.AuthenticationException;
import crud.exception.DAOException;
import crud.model.entities.Image;
import crud.repository.image.impl.ImageRepositoryImpl;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class DeleteImageCommand extends AbstractCommand {
    private String page = SUPPLIER_PRODUCTS;
    private final ImageRepositoryImpl repository;
    private final AuthService authService;

    public DeleteImageCommand(ImageRepositoryImpl repository, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
    }

    @Override
    public ServiceResult execute(HttpServletRequest request) {
        try{
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            UUID userId = authService.getUserId(token);
            Image image = repository.findById(UUID.fromString(request.getParameter("imageId")));

            if(!authService.isAllowed(token,UUID.fromString(image.getCreatedBy()))){
                throw new AuthenticationException("The user is not allowed to delete this Supplier");
            }

            repository.delete(image);
            addSuccessMessage(request,DELETE_SUCCESS_MESSAGE);

        } catch (DAOException ex){
            Logger.error(this.getClass().getName(), ex.getMessage());
            page = SUPPLIER_PRODUCTS;
            setException(request, ex);
        }
        return createView(page);
    }
}
