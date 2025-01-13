package crud.infrastructure;

import crud.repository.bill.impl.BillRepositoryImpl;
import crud.repository.product.impl.ProductRepositoryImpl;
import crud.repository.retailer.RetailerRepositoryImpl;
import crud.repository.supplier.SupplierRepositoryImpl;
import crud.service.bills.rules.BillBusinessRules;
import crud.service.products.rules.ProductBusinessRules;
import crud.service.retailers.rules.RetailerBusinessRules;
import crud.service.suppliers.rules.SupplierBusinessRules;

public class BusinessRulesFactory {
    private final RetailerBusinessRules retailerBusinessRules;
    private final SupplierBusinessRules supplierBusinessRules;
    private final ProductBusinessRules productBusinessRules;
    private final BillBusinessRules billBusinessRules;

    public BusinessRulesFactory(RetailerRepositoryImpl retailerRepository, ProductRepositoryImpl productRepository, SupplierRepositoryImpl supplierRepository, BillRepositoryImpl billRepositoryImpl) {
        this.retailerBusinessRules = new RetailerBusinessRules(retailerRepository);
        this.supplierBusinessRules = new SupplierBusinessRules(supplierRepository);
        this.productBusinessRules = new ProductBusinessRules(productRepository);
        this.billBusinessRules = new BillBusinessRules(billRepositoryImpl);
    }
    public RetailerBusinessRules getRetailerBusinessRules() {
        return retailerBusinessRules;
    }
    public SupplierBusinessRules getSupplierBusinessRules() {
        return supplierBusinessRules;
    }
    public ProductBusinessRules getProductBusinessRules() {
        return productBusinessRules;
    }
    public BillBusinessRules getBillBusinessRules() {
        return billBusinessRules;
    }

}
