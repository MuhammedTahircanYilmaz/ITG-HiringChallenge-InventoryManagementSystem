package crud.infrastructure;

import crud.repository.BillRepository;
import crud.repository.ProductRepository;
import crud.repository.RetailerRepository;
import crud.repository.SupplierRepository;
import crud.service.bills.rules.BillBusinessRules;
import crud.service.products.rules.ProductBusinessRules;
import crud.service.retailers.rules.RetailerBusinessRules;
import crud.service.suppliers.rules.SupplierBusinessRules;

public class BusinessRulesFactory {
    private final RetailerBusinessRules retailerBusinessRules;
    private final SupplierBusinessRules supplierBusinessRules;
    private final ProductBusinessRules productBusinessRules;
    private final BillBusinessRules billBusinessRules;
    private final RetailerRepository retailerRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final BillRepository billRepository;

    public BusinessRulesFactory(RetailerRepository retailerRepository, ProductRepository productRepository, SupplierRepository supplierRepository, BillRepository billRepository) {
        this.retailerRepository = retailerRepository;
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.billRepository = billRepository;

        this.retailerBusinessRules = new RetailerBusinessRules(retailerRepository);
        this.supplierBusinessRules = new SupplierBusinessRules(supplierRepository);
        this.productBusinessRules = new ProductBusinessRules(productRepository);
        this.billBusinessRules = new BillBusinessRules(billRepository);
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
