package crud.service.products.commands;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.base.ServiceResult;
import crud.dtos.products.requests.AddProductCommandDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.ProductMapper;
import crud.model.entities.Product;
import crud.repository.product.impl.ProductRepositoryImpl;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

public class AddProductCommand extends AbstractCommand {
    private final ProductRepositoryImpl repository;
    private final ProductMapper mapper;
    private final AuthService authService;

    private String page = CREATE_PRODUCT;

    public AddProductCommand(ProductRepositoryImpl repository, ProductMapper mapper , AuthService authService) {
        this.repository = repository;
        this.authService = authService;
        this.mapper = mapper;
    }

    @Override
    public ServiceResult execute(HttpServletRequest request) {
        try {
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);
            if(!authService.hasRole(token, "Supplier") ){
                throw new DAOException("The User is not allowed to add a Product");
            }

            AddProductCommandDto dto = mapper.mapAddRequestDto(request, authService.getUserId(token).toString());
            Product product = mapper.mapAddEntityDtoToEntity(dto);
            repository.add(product);

            addSuccessMessage(request, ADD_SUCCESS_MESSAGE);

        } catch(MappingException | DAOException ex)
        {
            Logger.error(this.getClass().getName(), ex.getMessage());
            page = CREATE_PRODUCT;
            setException(request, ex);
        }
        return createView( page);
    }
}
