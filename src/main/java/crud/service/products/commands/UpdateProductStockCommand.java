package crud.service.products.commands;


import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.dtos.products.requests.UpdateProductCommandDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.ProductMapper;
import crud.model.entities.Product;
import crud.repository.ProductRepository;
import crud.service.validation.ProductValidator;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class UpdateProductStockCommand extends AbstractCommand {

    public UpdateProductStockCommand(ProductRepository repository, ProductMapper mapper, ProductValidator validator, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
        this.validator = validator;
        this.mapper = mapper;
    }

    private String page = PAGE_PRODUCT_LIST;
    private final ProductRepository repository;
    private final ProductValidator validator ;
    private final ProductMapper mapper;
    private final AuthService authService;

    @Override
    public String execute(HttpServletRequest request) {

        try{
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            UUID userId = authService.getUserId(token);
            UUID productId = UUID.fromString(request.getParameter("productId"));
            validator.validateUpdateRequest(request);

            Product product = repository.findById(productId);
            authService.isAllowed(token,product.getSupplierId());

            UpdateProductCommandDto dto = mapper.mapUpdateRequestDto(request, userId.toString());

            product.setStockQuantity(dto.getStockQuantity());
            if( product.getStockQuantity() == 0){
                product.setInStock(false);
            }

            repository.update(product);
            addSuccessMessage(request,UPDATE_SUCCESS_MESSAGE);

        } catch(DAOException | MappingException ex){

            Logger.error(this.getClass().getName(), ex.getMessage());
            page = PAGE_PRODUCT_FORM;

            setException(request, ex);
        }
        return page;
    }

}