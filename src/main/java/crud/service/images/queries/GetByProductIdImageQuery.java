package crud.service.images.queries;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.exception.DAOException;
import crud.model.entities.Image;
import crud.repository.ImageRepository;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public class GetByProductIdImageQuery extends AbstractCommand {
    private final ImageRepository repository;
    private final AuthService authService;

    private String page = PAGE_PRODUCT_FORM;

    public GetByProductIdImageQuery(ImageRepository repository, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try {
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            UUID productId = UUID.fromString(request.getParameter("productId"));

            List<Image> images = repository.findAllByProductId(productId);

            setEntities(request, images, "No images found for the specified product.");

        } catch (DAOException ex) {
            Logger.error(this.getClass().getName(), ex.getMessage());
            page = PAGE_PRODUCT_FORM;
            setException(request, ex);
        }
        return page;
    }
}

