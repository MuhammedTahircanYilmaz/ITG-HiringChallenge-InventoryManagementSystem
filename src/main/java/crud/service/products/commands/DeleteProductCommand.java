package crud.service.products.commands;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.dtos.bills.requests.DeleteBillCommandDto;
import crud.dtos.products.requests.DeleteProductCommandDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.ProductMapper;
import crud.model.entities.Product;
import crud.repository.ProductRepository;
import crud.service.validation.ProductValidator;
import crud.util.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class DeleteProductCommand extends AbstractCommand {

    private final ProductRepository repository;
    private final ProductValidator validator;
    private final ProductMapper mapper;
    private final AuthService authService;
    private String page = PAGE_PRODUCT_LIST;

    public DeleteProductCommand(ProductRepository repository, ProductValidator validator, ProductMapper mapper, AuthService authService) {
        this.repository = repository;
        this.validator = validator;
        this.mapper = mapper;
        this.authService = authService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try{
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            UUID userId = authService.getUserId(token);
            UUID ProductId = UUID.fromString(request.getParameter("productId"));
            validator.validateDeleteRequest(request);

            Product product = repository.findById(ProductId);
            authService.isAllowed(token,product.getSupplierId());

            DeleteProductCommandDto dto = mapper.mapDeleteRequestDto(request, userId.toString());
            product.setDeletedBy(dto.getDeletedBy());
            product.setDeletedAt(dto.getDeletedAt());

            repository.delete(product);
            Logger.info(this.getClass().getName(), DELETE_SUCCESS_MESSAGE);

        }  catch (DAOException | MappingException ex){
            Logger.error(this.getClass().getName(), ex.getMessage());
            page = PAGE_BILL_FORM;
            setException(request, ex);
        }
        return page;
    }
}