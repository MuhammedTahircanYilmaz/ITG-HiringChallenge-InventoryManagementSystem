package crud.service.products.queries;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.dtos.products.responses.ProductResponseDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.ProductMapper;
import crud.model.entities.Product;
import crud.repository.ProductRepository;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;

public class GetAllProductQuery extends AbstractCommand {

        private ProductRepository repository;
        private String page = PAGE_PRODUCT_LIST;
        private AuthService authService;
        private final ProductMapper mapper;

        public GetAllProductQuery(ProductRepository repository, AuthService authService, ProductMapper mapper) {
            this.repository = repository;
            this.mapper = mapper;
            this.authService = authService;
        }

        @Override
        public String execute(HttpServletRequest request) {

            try{
                String token = authService.extractToken(request);
                authService.isAuthenticated(token);

                ArrayList<Product> products = repository.getAll();
                ArrayList<ProductResponseDto> response = new ArrayList<>();

                for(Product product : products){
                    ProductResponseDto dto = mapper.mapEntityToEntityResponseDto(product);
                    response.add(dto);
                }

                setEntities(request, response,"");
            } catch (DAOException | MappingException ex){

                Logger.error(this.getClass().getName(),ex.getMessage());
                setException(request, ex);

            }
            return page;
        }
    }
