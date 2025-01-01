package crud.service.suppliers.commands;

import crud.base.AbstractCommand;
import crud.base.BaseRepository;
import crud.exception.DAOException;
import crud.model.Supplier;
import crud.repository.SupplierRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class DeleteSupplierCommand extends AbstractCommand {

    private final SupplierRepository repository;
    private String page = PAGE_SUPPLIER_LIST;

    public DeleteSupplierCommand(SupplierRepository repository) {
        this.repository = repository;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try{
            //SupplierValidator.validateDeleteRequest(request);

            repository.delete(getId(request));
            addSuccessMessage(request,DELETE_SUCCESS_MESSAGE);

        } catch (DAOException | ServletException ex){
            //logger.error(ex.getMessage);
            page = PAGE_SUPPLIER_FORM;
            setException(request, ex);
        }
        return page;
    }
}
