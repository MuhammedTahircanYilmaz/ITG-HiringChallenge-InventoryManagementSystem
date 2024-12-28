public class DAOFactory {

    public static ProductDAO getProductDAO() throws DAOException {
        return new ProductDAO(ConnectionFactory.getConnection());
    }

    public static SupplierDAO getSupplierDAO() throws DAOException {
        return new SupplierDAO(ConnectionFactory.getConnection());
    }

    public static RetailerDAO getRetailerDAO() throws DAOException {
        return new RetailerDAO(ConnectionFactory.getConnection());
    }

    public static BillDAO getBillDAO() throws DAOException{
        return new BillDAO(ConnectionFactory.getConnection());
    }
}