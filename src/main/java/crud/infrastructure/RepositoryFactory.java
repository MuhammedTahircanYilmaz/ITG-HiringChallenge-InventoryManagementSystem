package crud.infrastructure;

import crud.exception.DAOException;

import crud.repository.*;


public class RepositoryFactory {
    private final BillRepository billRepository;
    private final ProductRepository productRepository;
    private final RetailerRepository retailerRepository;
    private final SupplierRepository supplierRepository;
    private final TokenRepository tokenRepository;
    private final AdminRepository adminRepository;

    public RepositoryFactory() throws DAOException {
        this.billRepository = new BillRepository(ConnectionFactory.getConnection());
        this.productRepository = new ProductRepository(ConnectionFactory.getConnection());
        this.retailerRepository = new RetailerRepository(ConnectionFactory.getConnection());
        this.supplierRepository = new SupplierRepository(ConnectionFactory.getConnection());
        this.tokenRepository = new TokenRepository(ConnectionFactory.getConnection());
        this.adminRepository = new AdminRepository(ConnectionFactory.getConnection());
    }

    public BillRepository getBillRepository() {
        return billRepository;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public RetailerRepository getRetailerRepository() {
        return retailerRepository;
    }

    public SupplierRepository getSupplierRepository() {
        return supplierRepository;
    }

    public TokenRepository getTokenRepository() {
        return tokenRepository;
    }
    public AdminRepository getAdminRepository() {
        return adminRepository;
    }
}