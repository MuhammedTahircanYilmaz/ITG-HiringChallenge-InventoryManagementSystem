package crud.service.suppliers.rules;

import crud.base.BaseBusinessRules;
import crud.model.entities.Supplier;
import crud.repository.SupplierRepository;

import java.util.UUID;

public class SupplierBusinessRules extends BaseBusinessRules<Supplier, SupplierRepository>{


    public SupplierBusinessRules(SupplierRepository repository) {
        super(repository);

    }

    public boolean isValidName(String name) {
        if(name.length() <= 0 || name.length() > 100 || name == null){
            return false;
        }
        return true;
    }

    public boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public boolean isValidPassword(String password) {

        if (password == null || password.isEmpty() || password.length() < 8) {
            return false;
        }

        return password != null && password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\d\\s])[A-Za-z\\d@$!%.*?&]{8,}$");
    }

}
