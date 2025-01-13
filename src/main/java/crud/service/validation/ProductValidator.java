package crud.service.validation;

import crud.base.BaseValidator;
import crud.model.entities.Product;
import crud.repository.product.impl.ProductRepositoryImpl;
import crud.service.products.rules.ProductBusinessRules;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class ProductValidator extends BaseValidator<Product, ProductBusinessRules, ProductRepositoryImpl> {
   private ProductBusinessRules rules;
   private ProductRepositoryImpl repository;

    public ProductValidator(ProductBusinessRules rules) {
        super(rules);
        this.rules = rules;
    }

    @Override
    public boolean validateAddRequest(HttpServletRequest request) {


        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        long quantity = Long.parseLong(request.getParameter("quantity"));
        float discount = Float.parseFloat(request.getParameter("discount"));

        if(!rules.productNameIsValid(name)
                || !rules.productPriceIsValid(price)
                || !rules.productQuantityIsValid(quantity)
                || !rules.productDiscountIsValid(discount)){

            throw new IllegalArgumentException("There are problems with the data. Please try again");
        }
        return true;
    }

    @Override
    public boolean validateUpdateRequest(HttpServletRequest request) {


        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        long quantity = Long.parseLong(request.getParameter("quantity"));
        float discount = Float.parseFloat(request.getParameter("discount"));

        if(!rules.productNameIsValid(name)
                || !rules.productPriceIsValid(price)
                || !rules.productQuantityIsValid(quantity)
                || !rules.productDiscountIsValid(discount)){
            throw new IllegalArgumentException("There are problems with the data. Please try again");

        }
        return true;
    }

    public boolean validateGetBySupplierIdRequest(HttpServletRequest request) {
        UUID supplierId = UUID.fromString(request.getParameter("supplierId"));
        return supplierId != null;
    }

}
