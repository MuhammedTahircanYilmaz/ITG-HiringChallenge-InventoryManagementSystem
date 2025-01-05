package crud.service.products.queries;

import crud.base.AbstractCommand;
import crud.exception.DAOException;
import crud.model.entities.Product;
import crud.repository.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;

public class GetAllProductQuery extends AbstractCommand {

        private ProductRepository repository;
        private String page = PAGE_PRODUCT_LIST;

        public GetAllProductQuery(ProductRepository repository) {
            this.repository = repository;
        }

        @Override
        public String execute(HttpServletRequest request) {

            try{
                ArrayList<Product> products = repository.getAll();

                setEntities(request, products,"");
            } catch (DAOException ex){

                //logger.error(ex.getMessage());
                setException(request, ex);
            }
            return page;
        }
    }
