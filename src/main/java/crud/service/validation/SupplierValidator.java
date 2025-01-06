package crud.service.validation;

import crud.base.BaseValidator;
import crud.exception.BusinessException;
import crud.model.entities.Supplier;
import crud.repository.SupplierRepository;
import crud.service.suppliers.rules.SupplierBusinessRules;
import jakarta.servlet.http.HttpServletRequest;

public class SupplierValidator extends BaseValidator<Supplier, SupplierBusinessRules, SupplierRepository> {
    private SupplierBusinessRules rules;

    public SupplierValidator(SupplierBusinessRules rules) {
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


        if(!rules.isValidPassword(oldPassword) || !rules.isValidPassword(newPassword)){
            throw new BusinessException("There are problems with the password");
        }

        return true;
    }

}
