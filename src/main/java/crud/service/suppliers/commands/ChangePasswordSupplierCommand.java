package crud.service.suppliers.commands;

import crud.base.AbstractCommand;
import crud.base.BaseRepository;
import crud.exception.BusinessException;
import crud.exception.DAOException;
import crud.model.Supplier;
import crud.repository.SupplierRepository;
import crud.util.PasswordUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.util.UUID;

public class ChangePasswordSupplierCommand extends AbstractCommand {

    private final SupplierRepository repository;
    private String page = PAGE_SUPPLIER_LIST;

    public ChangePasswordSupplierCommand(SupplierRepository repository) {
        this.repository = repository;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try{
           // SupplierValidator.validateChangePasswordRequest(request);

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
