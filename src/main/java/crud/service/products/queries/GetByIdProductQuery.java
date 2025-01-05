package crud.service.products.queries;

import crud.base.AbstractCommand;
import crud.exception.DAOException;
import crud.model.entities.Product;
import crud.repository.ProductRepository;
import crud.service.validation.ProductValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

public class GetByIdProductQuery extends AbstractCommand {

    private final ProductRepository repository;
    private final ProductValidator validator;
    private String page = PAGE_PRODUCT_FORM;


    public GetByIdProductQuery(ProductRepository repository, ProductValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try{
            validator.validateGetByIdRequest(request);
            Product product = repository.findById(getId(request));

            setEntity(request, product);
        } catch (DAOException | ServletException ex){

            //logger.error(ex.getMessage());
            page = PAGE_PRODUCT_LIST;

            setException(request, ex);
        }
        return page;
    }
}
