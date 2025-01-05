package crud.service.retailers.commands;

import crud.base.AbstractCommand;
import crud.exception.BusinessException;
import crud.exception.DAOException;
import crud.model.entities.Retailer;
import crud.repository.RetailerRepository;
import crud.service.validation.RetailerValidator;
import crud.util.PasswordUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

public class ChangePasswordRetailerCommand extends AbstractCommand {

    private final RetailerRepository repository;
    private final RetailerValidator validator;
    private String page = PAGE_RETAILER_LIST;

    public ChangePasswordRetailerCommand(RetailerRepository repository , RetailerValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try{

            validator.validateChangePasswordRequest(request);

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
