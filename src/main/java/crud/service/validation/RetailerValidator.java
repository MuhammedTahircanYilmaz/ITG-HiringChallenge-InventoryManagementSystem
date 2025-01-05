package crud.service.validation;

import crud.base.BaseValidator;
import crud.model.entities.Retailer;
import crud.repository.RetailerRepository;
import crud.service.retailers.rules.RetailerBusinessRules;
import jakarta.servlet.http.HttpServletRequest;

public class RetailerValidator extends BaseValidator<Retailer, RetailerBusinessRules, RetailerRepository> {
    private RetailerBusinessRules rules;

    public RetailerValidator(RetailerBusinessRules rules) {
        super(rules);
    }

    @Override
    public boolean validateAddRequest(HttpServletRequest request) {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if(!rules.isValidEmail(email) || !rules.isValidPassword(password) || !rules.isValidName(name)){
            return false;
        }
        return true;
    }

    @Override
    public boolean validateUpdateRequest(HttpServletRequest request) {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if(!rules.isValidEmail(email) || !rules.isValidPassword(password) || !rules.isValidName(name)){
            return false;
        }
        return true;
    }

    public boolean validateGetByNameRequest(HttpServletRequest request){
        String name = request.getParameter("name");
        return name != null;
    }

    public boolean validateChangePasswordRequest(HttpServletRequest request){
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if(!rules.isValidPassword(oldPassword) || !rules.isValidPassword(newPassword) || !rules.isValidPassword(confirmPassword)){
            return false;
        }

        if(!newPassword.equals(confirmPassword)){
            return false;
        }
        return true;
    }

}