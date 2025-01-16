package crud.service.products.queries;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.base.ServiceResult;
import crud.dtos.products.responses.ProductResponseDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.ProductMapper;
import crud.model.entities.Product;
import crud.repository.product.impl.ProductRepositoryImpl;
import crud.service.products.rules.ProductBusinessRules;
import crud.util.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class GetByIdProductQuery extends AbstractCommand {

    private final ProductRepositoryImpl repository;
    private final ProductBusinessRules rules;
    private final AuthService authService;
    private final ProductMapper mapper;
    private String page = PRODUCT;


    public GetByIdProductQuery(ProductRepositoryImpl repository, ProductBusinessRules rules, AuthService authService, ProductMapper mapper) {
        this.repository = repository;
        this.rules = rules;
        this.authService = authService;
        this.mapper = mapper;
    }

    @Override
    public ServiceResult execute(HttpServletRequest request) {
        try{
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            rules.entityExists(UUID.fromString(request.getParameter("id")));
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
