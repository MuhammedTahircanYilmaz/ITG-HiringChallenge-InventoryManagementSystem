package crud.infrastructure;

import crud.mapper.*;
import crud.repository.product.impl.ProductRepositoryImpl;
import crud.repository.supplier.SupplierRepositoryImpl;

public class MapperFactory {
    private final BillMapper billMapper;
    private final ProductMapper productMapper;
    private final RetailerMapper retailerMapper;
    private final SupplierMapper supplierMapper;
    private final SupplierRepositoryImpl supplierRepository;
    private final ProductRepositoryImpl productRepository;

    public MapperFactory(ProductRepositoryImpl productRepository, SupplierRepositoryImpl supplierRepository) {
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.billMapper = new BillMapper(productRepository);
        this.productMapper = new ProductMapper(supplierRepository);
        this.retailerMapper = new RetailerMapper();
        this.supplierMapper = new SupplierMapper();
    }

    public BillMapper getBillMapper() {
        return billMapper;
    }

    public ProductMapper getProductMapper() {
        return productMapper;
    }

    public RetailerMapper getRetailerMapper() {
        return retailerMapper;
    }

    public SupplierMapper getSupplierMapper() {
        return supplierMapper;
    }
}