package crud.service.suppliers.queries;

import crud.base.AbstractCommand;
import crud.exception.DAOException;
import crud.model.entities.Supplier;
import crud.repository.SupplierRepository;
import crud.service.validation.SupplierValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

public class GetByIdSupplierQuery extends AbstractCommand {

    private final SupplierRepository repository;
    private final SupplierValidator validator;
    private String page = PAGE_SUPPLIER_FORM;

    public GetByIdSupplierQuery(SupplierRepository repository, SupplierValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try{
            validator.validateGetByIdRequest(request);

            Supplier supplier = repository.findById(getId(request));

            setEntity(request, supplier);
        } catch (DAOException | ServletException ex){

            //logget.error(ex.getMessage());
            page = PAGE_SUPPLIER_LIST;

            setException(request, ex);
        }
        return page;
    }
}
