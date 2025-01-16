package crud.service.products.commands;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.base.ServiceResult;
import crud.dtos.products.requests.DeleteProductCommandDto;
import crud.exception.BusinessException;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.ProductMapper;
import crud.model.entities.Product;
import crud.repository.product.impl.ProductRepositoryImpl;
import crud.service.products.rules.ProductBusinessRules;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class DeleteProductCommand extends AbstractCommand {

    private final ProductRepositoryImpl repository;
    private ProductBusinessRules rules;
    private final ProductMapper mapper;
    private final AuthService authService;
    private String page = SUPPLIER_PRODUCTS;

    public DeleteProductCommand(ProductRepositoryImpl repository, ProductBusinessRules rules, ProductMapper mapper, AuthService authService) {
        this.repository = repository;
        this.rules = rules;
        this.mapper = mapper;
        this.authService = authService;
    }

    @Override
    public ServiceResult execute(HttpServletRequest request) {
        try{
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            UUID userId = authService.getUserId(token);
            UUID ProductId = UUID.fromString(request.getParameter("productId"));

            Product product = repository.findById(ProductId);

            authService.isAllowed(token,product.getSupplierId());

            DeleteProductCommandDto dto = mapper.mapDeleteRequestDto(request, userId.toString());
            product.setDeletedBy(dto.getDeletedBy());
            product.setDeletedAt(dto.getDeletedAt());

            repository.delete(product);
            Logger.info(this.getClass().getName(), DELETE_SUCCESS_MESSAGE);

        }  catch (DAOException | MappingException ex){
            Logger.error(this.getClass().getName(), ex.getMessage());
            page = SUPPLIER_PRODUCTS;
            setException(request, ex);
        }
        return createView( page);
    }
}