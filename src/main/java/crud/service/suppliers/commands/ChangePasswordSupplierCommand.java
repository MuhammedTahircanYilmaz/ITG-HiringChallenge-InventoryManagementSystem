package crud.service.suppliers.commands;

import crud.base.AbstractCommand;
import crud.exception.BusinessException;
import crud.exception.DAOException;
import crud.model.entities.Supplier;
import crud.repository.SupplierRepository;
import crud.service.validation.SupplierValidator;
import crud.util.PasswordUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

public class ChangePasswordSupplierCommand extends AbstractCommand {

    private final SupplierRepository repository;
    private final SupplierValidator validator;
    private String page = PAGE_SUPPLIER_LIST;

    public ChangePasswordSupplierCommand(SupplierRepository repository, SupplierValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try{
            validator.validateChangePasswordRequest(request);

            Supplier supplier = repository.findById(getId(request));

            String currentPassword = request.getParameter("currentPassword");
            PasswordUtils.verifyPassword(currentPassword, supplier.getPassword());

            String newPassword = request.getParameter("newPassword");
            supplier.setPassword(PasswordUtils.hashPassword(newPassword));

            repository.update(supplier);
        } catch (DAOException | BusinessException | ServletException ex){
            //logger.error(e.getMessage());

            page = PAGE_SUPPLIER_FORM;
            setException(request, ex);
        }
        return page;
    }
}
