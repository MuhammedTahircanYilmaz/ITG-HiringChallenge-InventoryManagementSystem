package crud.service.products.queries;

import crud.base.AbstractCommand;
import crud.exception.DAOException;
import crud.model.entities.Product;
import crud.repository.ProductRepository;
import crud.service.validation.ProductValidator;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.UUID;

public class GetBySupplierIdProductQuery extends AbstractCommand {

    private final ProductRepository repository;
    private final ProductValidator validator;
    private String page = PAGE_PRODUCT_FORM;

    public GetBySupplierIdProductQuery(ProductRepository repository, ProductValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try {
            UUID supplierId = UUID.fromString(request.getParameter("supplierId"));
            validator.validateGetBySupplierIdRequest(request);

            ArrayList<Product> Products = repository.findBySupplierId(supplierId);

            setEntities(request, Products, "");

        } catch (DAOException | IllegalArgumentException ex) {

            // logger.error(ex.getMessage());

            page = PAGE_PRODUCT_LIST;

            setException(request, ex);
        }
        return page;
    }

}

