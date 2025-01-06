package crud.service.images.commands;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.base.ServiceResult;
import crud.exception.BusinessException;
import crud.exception.DAOException;
import crud.model.entities.Image;
import crud.repository.ImageRepository;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class AddImageCommand extends AbstractCommand {
    private final ImageRepository repository;
    private final AuthService authService;
    private final String uploadDir;

    private String page = PRODUCT;

    public AddImageCommand(ImageRepository repository, AuthService authService, String uploadDir) {
        this.repository = repository;
        this.authService = authService;
        this.uploadDir = uploadDir;
    }

    @Override
    public ServiceResult execute(HttpServletRequest request) {
        try {
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            Part filePart = request.getPart("file");

            String uniqueFileName = UUID.randomUUID() + "_" + filePart.getSubmittedFileName();
            File savedFile = new File(uploadDir, uniqueFileName);
            filePart.write(savedFile.getAbsolutePath());

            Image image = new Image();
            image.setId(UUID.randomUUID());
            image.setImageLocation(savedFile.getAbsolutePath());
            image.setCreatedBy(authService.getUserId(token).toString());
            image.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            image.setProductId(request.getParameter("productId"));

            repository.add(image);

            addSuccessMessage(request, "Image uploaded successfully!");

        } catch (Exception ex) {
            Logger.error(this.getClass().getName(), ex.getMessage());
            page = PRODUCT;
            setException(request, ex);
        }
        return createView(page);
    }
}