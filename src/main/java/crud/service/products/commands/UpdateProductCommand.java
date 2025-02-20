package crud.service.products.commands;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.base.ServiceResult;
import crud.dtos.products.requests.UpdateProductCommandDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.ProductMapper;
import crud.model.entities.Product;
import crud.repository.product.impl.ProductRepositoryImpl;
import crud.service.products.rules.ProductBusinessRules;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class UpdateProductCommand extends AbstractCommand {

    public UpdateProductCommand(ProductRepositoryImpl repository, ProductMapper mapper, ProductBusinessRules rules, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
        this.rules = rules;
        this.mapper = mapper;
    }

    private String page = PRODUCT;
    private final ProductRepositoryImpl repository;
    private final ProductBusinessRules rules;
    private final ProductMapper mapper;
    private final AuthService authService;

    @Override
    public ServiceResult execute(HttpServletRequest request) {

        try{
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            UUID userId = authService.getUserId(token);
            UUID productId = UUID.fromString(request.getParameter("productId"));
            rules.entityExists(productId);

            Product product = repository.findById(productId);
            authService.isAllowed(token,product.getSupplierId());

            UpdateProductCommandDto dto = mapper.mapUpdateRequestDto(request, userId.toString());

            product.setUpdatedBy(dto.getUpdatedBy());
            product.setUpdatedAt(dto.getUpdatedAt());
            product.setStockQuantity(dto.getStockQuantity());
            product.setName(dto.getName());
            product.setPrice(dto.getPrice());
            product.setDiscount(dto.getDiscount());
            product.setImagePath(dto.getImageLocation());
            product.setDescription(dto.getDescription());

            repository.update(product);
            addSuccessMessage(request,UPDATE_SUCCESS_MESSAGE);

        } catch(DAOException | MappingException ex){

            Logger.error(this.getClass().getName(), ex.getMessage());
            page = PRODUCT;

            setException(request, ex);
        }
        return createView(page);
    }

}