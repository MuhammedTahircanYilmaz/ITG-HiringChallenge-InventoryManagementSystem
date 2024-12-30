package crud.repository;

import crud.base.BaseRepository;
import crud.exception.DAOException;
import crud.infrastructure.ConnectionFactory;
import crud.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class ProductRepository implements BaseRepository<Product, UUID> {

    private Connection connection;

    private static final String SQL_INSERT = "INSERT INTO Products (Id, SupplierId, Name, Description, StockQuantity, Price, Discount, ImagePath) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE Products SET SupplierId = ?, Name = ?, Description = ?, StockQuantity = ?, Price = ?, Discount = ?, ImagePath = ? WHERE Id = ?";
    private static final String SQL_DELETE = "DELETE FROM Products WHERE Id = ?";
    private static final String SQL_FIND_ALL = "SELECT * FROM Products";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM Products WHERE Id = ?";
    private static final String SQL_FIND_BY_NAME = "SELECT * FROM Products WHERE Name LIKE ?";


    public ProductRepository(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        this.connection = connection;
    }

    @Override
    public void add(Product entity) throws DAOException {
        if (entity == null) {
            throw new DAOException("Product entity cannot be null");
        }

        PreparedStatement ps = null;
        try {
            UUID id = UUID.randomUUID();
            ps = connection.prepareStatement(SQL_INSERT);

            ps.setString(1, id.toString());
            ps.setLong(2, entity.getSupplierId());
            ps.setString(3, entity.getName());
            ps.setString(4, entity.getDescription());
            ps.setLong(5, entity.getStockQuantity());
            ps.setDouble(6, entity.getPrice());
            ps.setFloat(7, entity.getDiscount());
            ps.setString(8, entity.getImageLocation());

            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DAOException("Error while adding the Product: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnectionAndStatement(connection, ps);
        }
    }

    @Override
    public Product update(Product entity) throws DAOException {
        if (entity == null) {
            throw new DAOException("Product entity cannot be null");
        }

        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(SQL_UPDATE);

            ps.setLong(1, entity.getSupplierId());
            ps.setString(2, entity.getName());
            ps.setString(3, entity.getDescription());
            ps.setLong(4, entity.getStockQuantity());
            ps.setDouble(5, entity.getPrice());
            ps.setFloat(6, entity.getDiscount());
            ps.setString(7, entity.getImageLocation());
            ps.setString(8, entity.getId().toString());

            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DAOException("Error while updating the Product: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnectionAndStatement(connection, ps);
        }
        return entity;
    }

    @Override
    public void delete(UUID id) throws DAOException {
        if (id == null) {
            throw new DAOException("Product ID cannot be null");
        }

        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(SQL_DELETE);
            ps.setString(1, id.toString());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("No Product found with ID: " + id);
            }
        } catch (SQLException ex) {
            throw new DAOException("Error while deleting the Product: " + ex.getMessage(), ex);
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
            ps = connection.prepareStatement(SQL_FIND_BY_ID);
            ps.setString(1, id.toString());
            rs = ps.executeQuery();

            if (rs.next()) {
                product = getProduct(rs);
            }
        } catch (SQLException ex) {
            throw new DAOException("Error while finding the Product: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeAll(connection, ps, rs);
        }
        return product;
    }

    public ArrayList<Product> findAllByName(String name) throws DAOException {
        if (name == null || name.trim().isEmpty()) {
            throw new DAOException("Product name cannot be null or empty");
        }

        ArrayList<Product> products = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(SQL_FIND_BY_NAME);
            ps.setString(1, "%" + name + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                products.add(getProduct(rs));
            }
        } catch (SQLException ex) {
            throw new DAOException("Error while finding Products by name: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeAll(connection, ps, rs);
        }
        return products;
    }

    @Override
    public ArrayList<Product> getAll() throws DAOException {
        ArrayList<Product> products = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(SQL_FIND_ALL);
            rs = ps.executeQuery();

            while (rs.next()) {
                products.add(getProduct(rs));
            }
        } catch (SQLException ex) {
            throw new DAOException("Error while fetching all Products: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeAll(connection, ps, rs);
        }
        return products;
    }

    private Product getProduct(ResultSet rs) throws SQLException {
        if (rs == null) {
            throw new SQLException("ResultSet cannot be null");
        }

        Product product = new Product();

        product.setId(UUID.fromString(rs.getString("Id")));
        product.setSupplierId(rs.getLong("SupplierId"));
        product.setName(rs.getString("Name"));
        product.setDescription(rs.getString("Description"));
        product.setStockQuantity(rs.getLong("StockQuantity"));
        product.setPrice(rs.getDouble("Price"));
        product.setDiscount(rs.getFloat("Discount"));
        product.setImageLocation(rs.getString("ImagePath"));

        return product;
    }
}
