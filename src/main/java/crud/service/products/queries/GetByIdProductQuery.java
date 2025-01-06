package crud.service.products.queries;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.base.ServiceResult;
import crud.dtos.products.responses.ProductResponseDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.ProductMapper;
import crud.model.entities.Product;
import crud.repository.ProductRepository;
import crud.service.validation.ProductValidator;
import crud.util.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

public class GetByIdProductQuery extends AbstractCommand {

    private final ProductRepository repository;
    private final ProductValidator validator;
    private final AuthService authService;
    private final ProductMapper mapper;
    private String page = PRODUCT;


    public GetByIdProductQuery(ProductRepository repository, ProductValidator validator, AuthService authService, ProductMapper mapper) {
        this.repository = repository;
        this.validator = validator;
        this.authService = authService;
        this.mapper = mapper;
    }

    @Override
    public ServiceResult execute(HttpServletRequest request) {
        try{
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            validator.validateGetByIdRequest(request);
            Product product = repository.findById(getId(request));
            ProductResponseDto dto = mapper.mapEntityToEntityResponseDto(product);

            return createJsonResponse(dto);

        } catch (DAOException | ServletException | MappingException ex){

            Logger.error(this.getClass().getName(), ex.getMessage());
            page = PRODUCT;

            setException(request, ex);
        }
        return createView(page);
    }
}
