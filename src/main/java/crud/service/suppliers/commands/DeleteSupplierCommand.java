package crud.service.suppliers.commands;

import crud.base.AbstractCommand;
import crud.exception.DAOException;
import crud.repository.SupplierRepository;
import crud.service.validation.SupplierValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

public class DeleteSupplierCommand extends AbstractCommand {

    private final SupplierRepository repository;
    private final SupplierValidator validator;
    private String page = PAGE_SUPPLIER_LIST;

    public DeleteSupplierCommand(SupplierRepository repository, SupplierValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try{
            validator.validateDeleteRequest(request);

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
