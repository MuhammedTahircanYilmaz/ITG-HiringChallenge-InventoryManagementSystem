package crud.service.validation;

import crud.base.BaseValidator;
import crud.model.entities.Product;
import crud.repository.ProductRepository;
import crud.service.products.rules.ProductBusinessRules;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class ProductValidator extends BaseValidator<Product, ProductBusinessRules, ProductRepository> {
   private ProductBusinessRules rules;
   private ProductRepository repository;

    public ProductValidator(ProductBusinessRules rules) {
        super(rules);
    }

    @Override
    public boolean validateAddRequest(HttpServletRequest request) {
        boolean isValid = true;

        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        long quantity = Long.parseLong(request.getParameter("quantity"));
        float discount = Float.parseFloat(request.getParameter("discount"));

        if(!rules.productNameIsValid(name)
                || !rules.productPriceIsValid(price)
                || !rules.productQuantityIsValid(quantity)
                || !rules.productDiscountIsValid(discount)){
            isValid = false;
        }
        return isValid;
    }

    @Override
    public boolean validateUpdateRequest(HttpServletRequest request) {
        boolean isValid = true;

        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        long quantity = Long.parseLong(request.getParameter("quantity"));
        float discount = Float.parseFloat(request.getParameter("discount"));

        if(!rules.productNameIsValid(name)
                || !rules.productPriceIsValid(price)
                || !rules.productQuantityIsValid(quantity)
                || !rules.productDiscountIsValid(discount)){
            isValid = false;
        }
        return isValid;
    }

    public boolean validateGetBySupplierIdRequest(HttpServletRequest request) {
        UUID supplierId = UUID.fromString(request.getParameter("supplierId"));
        return supplierId != null;
    }

}
