package crud.service.retailers.commands;

import crud.base.AbstractCommand;
import crud.exception.BusinessException;
import crud.exception.DAOException;
import crud.model.Retailer;
import crud.repository.RetailerRepository;
import crud.util.PasswordUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

public class ChangePasswordRetailerCommand extends AbstractCommand {

    private final RetailerRepository repository;
    private String page = PAGE_RETAILER_LIST;

    public ChangePasswordRetailerCommand(RetailerRepository repository) {
        this.repository = repository;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try{
           // retailerValidator.validateChangePasswordRequest(request);

            Retailer retailer = repository.findById(getId(request));

            String currentPassword = request.getParameter("currentPassword");
            PasswordUtils.verifyPassword(currentPassword, retailer.getPassword());

            String newPassword = request.getParameter("newPassword");
            retailer.setPassword(PasswordUtils.hashPassword(newPassword));

            repository.update(retailer);
        } catch (DAOException | BusinessException | ServletException ex){
            //logger.error(e.getMessage());

            page = PAGE_RETAILER_FORM;
            setException(request, ex);
        }
        return page;
    }
}
