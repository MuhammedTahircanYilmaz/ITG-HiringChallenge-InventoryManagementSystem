package crud.infrastructure;

import crud.exception.DAOException;

import crud.repository.*;
import crud.repository.admin.impl.AdminRepositoryImpl;
import crud.repository.bill.impl.BillRepositoryImpl;
import crud.repository.image.impl.ImageRepositoryImpl;
import crud.repository.product.impl.ProductRepositoryImpl;
import crud.repository.retailer.RetailerRepositoryImpl;
import crud.repository.supplier.SupplierRepositoryImpl;


public class RepositoryFactory {
    private final BillRepositoryImpl billRepositoryImpl;
    private final ProductRepositoryImpl productRepository;
    private final RetailerRepositoryImpl retailerRepository;
    private final SupplierRepositoryImpl supplierRepository;
    private final TokenRepositoryImpl tokenRepository;
    private final AdminRepositoryImpl adminRepositoryImpl;
    private final ImageRepositoryImpl imageRepository;

    public RepositoryFactory() throws DAOException {
        this.billRepositoryImpl = new BillRepositoryImpl(ConnectionFactory.getConnection());
        this.productRepository = new ProductRepositoryImpl(ConnectionFactory.getConnection());
        this.retailerRepository = new RetailerRepositoryImpl(ConnectionFactory.getConnection());
        this.supplierRepository = new SupplierRepositoryImpl(ConnectionFactory.getConnection());
        this.tokenRepository = new TokenRepositoryImpl(ConnectionFactory.getConnection());
        this.adminRepositoryImpl = new AdminRepositoryImpl(ConnectionFactory.getConnection());
        this.imageRepository = new ImageRepositoryImpl(ConnectionFactory.getConnection());
    }

    public BillRepositoryImpl getBillRepository() {
        return billRepositoryImpl;
    }

    public ProductRepositoryImpl getProductRepository() {
        return productRepository;
    }

    public RetailerRepositoryImpl getRetailerRepository() {
        return retailerRepository;
    }

    public SupplierRepositoryImpl getSupplierRepository() {
        return supplierRepository;
    }

    public TokenRepositoryImpl getTokenRepository() {
        return tokenRepository;
    }
    public AdminRepositoryImpl getAdminRepository() {
        return adminRepositoryImpl;
    }
    public ImageRepositoryImpl getImageRepository() {
        return imageRepository;
    }
}