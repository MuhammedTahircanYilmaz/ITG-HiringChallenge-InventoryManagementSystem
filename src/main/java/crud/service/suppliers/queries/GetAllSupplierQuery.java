package crud.service.suppliers.queries;

import crud.base.AbstractCommand;
import crud.base.BaseRepository;
import crud.exception.DAOException;
import crud.model.Supplier;
import crud.repository.SupplierRepository;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;


public class GetAllSupplierQuery extends AbstractCommand {
    public GetAllSupplierQuery(SupplierRepository repository) {
        this.repository = repository;
    }

    private SupplierRepository repository;
    private String page = PAGE_SUPPLIER_LIST;

    @Override
    public String execute(HttpServletRequest request) {
        try{

            ArrayList<Supplier> suppliers = repository.getAll();

            setEntities(request, suppliers,"");

        } catch (DAOException ex){
            // logger.error(ex.getMessage());
            setException(request, ex);

        }
        return page;
    }
}
