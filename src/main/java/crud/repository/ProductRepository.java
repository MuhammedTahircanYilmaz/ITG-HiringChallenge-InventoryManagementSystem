package crud.repository;

import crud.base.BaseRepository;
import crud.exception.DAOException;
import crud.infrastructure.ConnectionFactory;
import crud.model.entities.Product;
import crud.model.entities.Retailer;
import crud.util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class ProductRepository implements BaseRepository<Product> {

    private Connection connection;


    private static final String SQL_INSERT = "INSERT INTO Products (Id, SupplierId, Name, Description, StockQuantity, Price, " +
            "Discount, ImagePath , CreatedBy, CreatedAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE Products SET SupplierId = ?, Name = ?, Description = ?, StockQuantity = ?," +
            " Price = ?, Discount = ?, ImagePath = ? , UpdatedBy = ?, UpdatedAt = ? WHERE Id = ? AND Deleted = false";
    private static final String SQL_DELETE = "Update Products SET Deleted = True, DeletedBy = ?, DeletedAt = ? WHERE Id = ? AND Deleted = false";
    private static final String SQL_FIND_ALL = "SELECT * FROM Products AND Deleted = false";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM Products WHERE Id = ? AND Deleted = false";
    private static final String SQL_FIND_BY_SUPPLIER_ID = "SELECT * FROM Products WHERE SupplierId = ? AND Deleted = false";
    private static final String SQL_FIND_BY_NAME = "SELECT * FROM Products WHERE Name LIKE ? AND Deleted = false";



    public ProductRepository(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        this.connection = connection;
    }

    @Override
    public Product add(Product entity) throws DAOException {
        if (entity == null) {
            throw new DAOException("Product entity cannot be null");
        }

        PreparedStatement ps = null;
        try {
            connection.setAutoCommit(false);

            UUID id = UUID.randomUUID();
            ps = connection.prepareStatement(SQL_INSERT);

            ps.setString(1, id.toString());
            ps.setString(2, entity.getSupplierId().toString());
            ps.setString(3, entity.getName());
            ps.setString(4, entity.getDescription());
            ps.setLong(5, entity.getStockQuantity());
            ps.setDouble(6, entity.getPrice());
            ps.setFloat(7, entity.getDiscount());
            ps.setString(8, entity.getImageLocation());
            ps.setString(9, entity.getCreatedBy());
            ps.setTimestamp(10, entity.getCreatedAt());

            ps.executeUpdate();
            connection.commit();

        } catch (SQLException ex) {
            try {
                if (connection != null) {
                    connection.rollback(); 
                }
            } catch (SQLException rollbackEx) {
                Logger.error(this.getClass().getName(),
                        "Error rolling back transaction: " + rollbackEx.getMessage());
            }
            throw new DAOException("Error while adding the Product: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnectionAndStatement(connection, ps);
        }
        return entity;
    }

    @Override
    public Product update(Product entity) throws DAOException {
        if (entity == null) {
            throw new DAOException("Product entity cannot be null");
        }

        PreparedStatement ps = null;
        try {
            connection.setAutoCommit(false);

            ps = connection.prepareStatement(SQL_UPDATE);

            ps.setString(1, entity.getSupplierId().toString());
            ps.setString(2, entity.getName());
            ps.setString(3, entity.getDescription());
            ps.setLong(4, entity.getStockQuantity());
            ps.setDouble(5, entity.getPrice());
            ps.setFloat(6, entity.getDiscount());
            ps.setString(7, entity.getImageLocation());
            ps.setString(8, entity.getUpdatedBy());
            ps.setTimestamp(9, entity.getUpdatedAt());
            ps.setString(10, entity.getId().toString());

            ps.executeUpdate();
            connection.commit();
        } catch (SQLException ex) {
            try {
                if (connection != null) {
                    connection.rollback(); 
                }
            } catch (SQLException rollbackEx) {
                Logger.error(this.getClass().getName(),
                        "Error rolling back transaction: " + rollbackEx.getMessage());
            }
            throw new DAOException("Error while adding the Product: " + ex.getMessage(), ex);
        }finally {
            ConnectionFactory.closeConnectionAndStatement(connection, ps);
        }
        return entity;
    }

    @Override
    public void delete(Product product) throws DAOException {
        if (product == null) {
            throw new DAOException("Product cannot be null");
        }

        PreparedStatement ps = null;
        try {
            connection.setAutoCommit(false);

            ps = connection.prepareStatement(SQL_DELETE);
            ps.setString(1, product.getDeletedBy());
            ps.setTimestamp(2, product.getDeletedAt());
            ps.setString(3, product.getId().toString());



            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("No Product found with ID: " + product.getId());
            }
            connection.commit();
        } catch (SQLException ex) {
            try {
                if (connection != null) {
                    connection.rollback();  // Rollback on error
                }
            } catch (SQLException rollbackEx) {
                Logger.error(this.getClass().getName(),
                        "Error rolling back transaction: " + rollbackEx.getMessage());
            }
            throw new DAOException("Error while adding the Product: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnectionAndStatement(connection, ps);
        }
    }

    @Override
    public Product findById(UUID id) throws DAOException {
        if (id == null) {
            throw new DAOException("Product ID cannot be null");
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        Product product = null;

        try {
            connection.setAutoCommit(false);

            ps = connection.prepareStatement(SQL_FIND_BY_ID);
            ps.setString(1, id.toString());
            rs = ps.executeQuery();

            if (rs.next()) {
                product = getProduct(rs);
            }
            connection.commit();
        } catch (SQLException ex) {
            try {
                if (connection != null) {
                    connection.rollback();  // Rollback on error
                }
            } catch (SQLException rollbackEx) {
                Logger.error(this.getClass().getName(),
                        "Error rolling back transaction: " + rollbackEx.getMessage());
            }
            throw new DAOException("Error while adding the Product: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeAll(connection, ps, rs);
        }
        return product;
    }

    public ArrayList<Product> findBySupplierId(UUID id) throws DAOException {
        if(id == null){
            throw new DAOException("The Supplier Id cannot be null");
        }

        ArrayList<Product> Products = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            connection.setAutoCommit(false);

            ps = connection.prepareStatement(SQL_FIND_BY_SUPPLIER_ID);
            ps.setString(1,id.toString());
            rs = ps.executeQuery();

            while (rs.next()){
                Products.add(getProduct(rs));
            }
            connection.commit();

        } catch (SQLException ex) {
            try {
                if (connection != null) {
                    connection.rollback();  // Rollback on error
                }
            } catch (SQLException rollbackEx) {
                Logger.error(this.getClass().getName(),
                        "Error rolling back transaction: " + rollbackEx.getMessage());
            }
            throw new DAOException("Error while adding the Product: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeAll(connection,ps,rs);
        }
        return Products;
    }


    public ArrayList<Product> findAllByName(String name) throws DAOException {
        if (name == null || name.trim().isEmpty()) {
            throw new DAOException("Product name cannot be null or empty");
        }

        ArrayList<Product> products = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection.setAutoCommit(false);

            ps = connection.prepareStatement(SQL_FIND_BY_NAME);
            ps.setString(1, "%" + name + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                products.add(getProduct(rs));
            }
            connection.commit();

        } catch (SQLException ex) {
            try {
                if (connection != null) {
                    connection.rollback();  // Rollback on error
                }
            } catch (SQLException rollbackEx) {
                Logger.error(this.getClass().getName(),
                        "Error rolling back transaction: " + rollbackEx.getMessage());
            }
            throw new DAOException("Error while adding the Product: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeAll(connection, ps, rs);
        }
        return products;
    }

    @Override
    public ArrayList<Product> getAll() throws DAOException {
        ArrayList<Product> retailers = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(SQL_FIND_ALL);
            rs = ps.executeQuery();

            while (rs.next()) {
                retailers.add(getProduct(rs));
            }
        } catch (SQLException ex) {
            throw new DAOException("Error while fetching all Prodcts: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeAll(connection, ps, rs);
        }
        return retailers;
    }



    private Product getProduct(ResultSet rs) throws SQLException {
        if (rs == null) {
            throw new SQLException("ResultSet cannot be null");
        }

        Product product = new Product();

        product.setId(UUID.fromString(rs.getString("Id")));
        product.setSupplierId(UUID.fromString(rs.getString("SupplierId")));
        product.setName(rs.getString("Name"));
        product.setDescription(rs.getString("Description"));
        product.setStockQuantity(rs.getLong("StockQuantity"));
        product.setPrice(rs.getDouble("Price"));
        product.setDiscount(rs.getFloat("Discount"));
        product.setImageLocation(rs.getString("ImagePath"));

        return product;
    }
}
