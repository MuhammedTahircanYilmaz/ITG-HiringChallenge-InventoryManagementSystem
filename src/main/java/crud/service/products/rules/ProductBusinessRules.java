package crud.service.products.rules;

import crud.base.BaseBusinessRules;
import crud.model.entities.Product;
import crud.repository.product.impl.ProductRepositoryImpl;

public class ProductBusinessRules extends BaseBusinessRules<Product, ProductRepositoryImpl> {


    public ProductBusinessRules(ProductRepositoryImpl repository) {
        super(repository);
    }

    public boolean productNameIsInvalid(String name) {
        if(name.length() < 2 || name.length() > 100){
            return true;
        }
        return false;
    }

    public boolean productPriceIsValid(double price) {
        if(price <= 0.01 || price >= 9999999.99){
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
