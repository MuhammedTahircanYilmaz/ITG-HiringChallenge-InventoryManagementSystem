package crud.service.retailers.rules;

import crud.base.BaseBusinessRules;
import crud.model.entities.Retailer;
import crud.repository.RetailerRepository;
import crud.service.suppliers.rules.SupplierBusinessRules;

import java.util.UUID;

public class RetailerBusinessRules extends BaseBusinessRules<Retailer,RetailerRepository> {


    public RetailerBusinessRules(RetailerRepository repository) {
        super(repository);
    }

    public static boolean isValidName(String name) {
        if(name.length() <= 0 || name.length() > 100 || name == null){
            return false;
        }
        return true;
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty() || password.length() < 8) {
            return false;
        }

        return password != null && password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\d\\s])[A-Za-z\\d@$!%*?&]{8,}$");
    }

}
