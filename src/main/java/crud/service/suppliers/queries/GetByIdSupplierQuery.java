package crud.service.suppliers.queries;

import crud.base.AbstractCommand;
import crud.exception.DAOException;
import crud.model.Supplier;
import crud.repository.SupplierRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class GetByIdSupplierQuery extends AbstractCommand {

    private final SupplierRepository repository;
    private String page = PAGE_SUPPLIER_FORM;

    public GetByIdSupplierQuery(SupplierRepository repository) {
        this.repository = repository;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try{
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
