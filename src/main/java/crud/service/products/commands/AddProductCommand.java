package crud.service.products.commands;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.dtos.products.requests.AddProductCommandDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.ProductMapper;
import crud.model.entities.Product;
import crud.repository.ProductRepository;
import crud.service.validation.ProductValidator;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

public class AddProductCommand extends AbstractCommand {
    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final ProductValidator validator;
    private final AuthService authService;

    private String page = PAGE_PRODUCT_FORM;

    public AddProductCommand(ProductRepository repository, ProductRepository productRepository, ProductMapper mapper, ProductValidator validator , AuthService authService) {
        this.repository = repository;
        this.validator = validator;
        this.authService = authService;
        this.mapper = mapper;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try {
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);
            if(!authService.hasRole(token, "Supplier") ){
                throw new DAOException("The User is not allowed to add a Product");
            }

            AddProductCommandDto dto = mapper.mapAddRequestDto(request, authService.getUserId(token).toString());
            validator.validateAddRequest(request);
            Product product = mapper.mapAddEntityDtoToEntity(dto);
            repository.add(product);

            addSuccessMessage(request, ADD_SUCCESS_MESSAGE);

        } catch(MappingException | DAOException ex)
        {
            Logger.error(this.getClass().getName(), ex.getMessage());
            page = PAGE_PRODUCT_FORM;
            setException(request, ex);
        }
        return page;
    }
}
