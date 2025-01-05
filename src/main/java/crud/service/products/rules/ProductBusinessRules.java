package crud.service.products.rules;

import crud.base.BaseBusinessRules;
import crud.exception.DAOException;
import crud.model.entities.Product;
import crud.repository.ProductRepository;

import java.util.UUID;

public class ProductBusinessRules extends BaseBusinessRules<Product, ProductRepository> {


    public ProductBusinessRules(ProductRepository repository) {
        super(repository);
    }

    public boolean productNameIsValid(String name) {
        if(name.length() <= 0 || name.length() > 100 || name == null){
            return false;
        }
        return true;
    }

    public boolean productPriceIsValid(double price) {
        if(price <= 0.01 || price >= 9999999.99){
            return false;
        }
        return true;
    }

    public boolean productQuantityIsValid(long quantity) {
        if(quantity <= 0){
            return false;
        }
        return true;
    }
    public boolean productDiscountIsValid(float discount) {
        if (discount < 0.01 || discount > 100) {
            return false;
        }
        return true;
    }

}