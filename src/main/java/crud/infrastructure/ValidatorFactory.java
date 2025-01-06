package crud.infrastructure;

import crud.service.bills.rules.BillBusinessRules;
import crud.service.products.rules.ProductBusinessRules;
import crud.service.retailers.rules.RetailerBusinessRules;
import crud.service.suppliers.rules.SupplierBusinessRules;
import crud.service.validation.BillValidator;
import crud.service.validation.ProductValidator;
import crud.service.validation.RetailerValidator;
import crud.service.validation.SupplierValidator;

public class ValidatorFactory {

    private final BillBusinessRules billRules;
    private final ProductBusinessRules productRules;
    private final SupplierBusinessRules supplierRules;
    private final RetailerBusinessRules retailerRules;

    public ValidatorFactory
            ( BillBusinessRules billRules, ProductBusinessRules productRules,
              SupplierBusinessRules supplierRules, RetailerBusinessRules retailerRules) {
        this.billRules = billRules;
        this.productRules = productRules;
        this.supplierRules = supplierRules;
        this.retailerRules = retailerRules;
    }

    public BillValidator getBillValidator() { return new BillValidator(billRules); }
    public ProductValidator getProductValidator() {return new ProductValidator(productRules);}
    public SupplierValidator getSupplierValidator(){return new SupplierValidator(supplierRules);}
    public RetailerValidator getRetailerValidator(){return new RetailerValidator(retailerRules);}
}
