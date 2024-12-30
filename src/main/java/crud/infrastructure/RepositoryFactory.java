package crud.infrastructure;

import crud.exception.DAOException;
import crud.repository.BillRepository;
import crud.repository.ProductRepository;
import crud.repository.RetailerRepository;
import crud.repository.SupplierRepository;

public class RepositoryFactory {

    public static ProductRepository getProductDAO() throws DAOException {
        return new ProductRepository(ConnectionFactory.getConnection());
    }

    public static SupplierRepository getSupplierDAO() throws DAOException {
        return new SupplierRepository(ConnectionFactory.getConnection());
    }

    public static RetailerRepository getRetailerDAO() throws DAOException {
        return new RetailerRepository(ConnectionFactory.getConnection());
    }

    public static BillRepository getBillDAO() throws DAOException {
        return new BillRepository(ConnectionFactory.getConnection());
    }
}