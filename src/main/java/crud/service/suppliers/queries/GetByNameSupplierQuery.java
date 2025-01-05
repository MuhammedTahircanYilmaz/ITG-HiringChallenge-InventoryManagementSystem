package crud.service.suppliers.queries;

import crud.base.AbstractCommand;
import crud.exception.DAOException;
import crud.model.entities.Supplier;
import crud.repository.SupplierRepository;
import crud.service.validation.SupplierValidator;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;

public class GetByNameSupplierQuery extends AbstractCommand {

    private final SupplierRepository repository;
    private final SupplierValidator validator;
    private String page = PAGE_SUPPLIER_FORM;

    public GetByNameSupplierQuery(SupplierRepository repository, SupplierValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try {
            validator.validateGetByNameRequest(request);
            String supplierName = request.getParameter("name");
            if (supplierName == null || supplierName.trim().isEmpty()) {

                throw new IllegalArgumentException("Supplier name cannot be null or empty");
            }

            ArrayList<Supplier> supplier = repository.findAllByName(supplierName);

            setEntity(request, supplier);

        } catch (DAOException  | IllegalArgumentException ex) {

            // logger.error(ex.getMessage());

            page = PAGE_SUPPLIER_LIST;

            setException(request, ex);
        }
        return page;
    }
}