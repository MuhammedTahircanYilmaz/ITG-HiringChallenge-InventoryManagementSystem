package crud.repository.product.impl;

import crud.base.AbstractRepository;
import crud.exception.DAOException;
import crud.infrastructure.ConnectionFactory;
import crud.model.entities.Product;
import crud.repository.product.ProductRepository;
import crud.util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ProductRepositoryImpl extends AbstractRepository implements ProductRepository {

    private Connection connection;
    private final String tableName = "products";
    private final String className = this.getClass().getName();


    public ProductRepositoryImpl(Connection connection) {
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

        String query = "INSERT INTO products (id, supplier_id, name, description, stock_quantity, price, " +
                "discount, image_path , created_by, created_at, in_stock ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,true)";

        PreparedStatement ps = null;

        try {
            connection.setAutoCommit(false);

            UUID id = UUID.randomUUID();
            ps = connection.prepareStatement(query);

            ps.setString(1, id.toString());
            ps.setString(2, entity.getSupplierId().toString());
            ps.setString(3, entity.getName());
            ps.setString(4, entity.getDescription());
            ps.setLong(5, entity.getStockQuantity());
            ps.setDouble(6, entity.getPrice());
            ps.setFloat(7, entity.getDiscount());
            ps.setString(8, entity.getImagePath());
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
                Logger.error(className,
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
        String query = "UPDATE products SET supplier_id = ?, name = ?, description = ?, stock_quantity = ?," +
                " price = ?, discount = ?, image_path = ? , updated_by = ?, updated_at = ? , in_stock = ?, WHERE id = ? AND deleted = false";

        try {
            connection.setAutoCommit(false);

            ps = connection.prepareStatement(query);

            ps.setString(1, entity.getSupplierId().toString());
            ps.setString(2, entity.getName());
            ps.setString(3, entity.getDescription());
            ps.setLong(4, entity.getStockQuantity());
            ps.setDouble(5, entity.getPrice());
            ps.setFloat(6, entity.getDiscount());
            ps.setString(7, entity.getImagePath());
            ps.setString(8, entity.getUpdatedBy());
            ps.setTimestamp(9, entity.getUpdatedAt());
            ps.setBoolean(10, entity.isInStock());
            ps.setString(11, entity.getId().toString());

            ps.executeUpdate();

            connection.commit();
        } catch (SQLException ex) {
            try {
                if (connection != null) {
                    connection.rollback(); 
                }
            } catch (SQLException rollbackEx) {
                Logger.error(className,
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
        String query;
        try {
            connection.setAutoCommit(false);
            query = softDeleteQuery(tableName, product.getId().toString());

            ps = connection.prepareStatement(query);
            ps.setString(2, product.getDeletedBy());
            ps.setTimestamp(1, product.getDeletedAt());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("No Product found with ID: " + product.getId());
            }
            connection.commit();

        } catch (SQLException ex) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                Logger.error(className,
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
        String query;

        try {
            connection.setAutoCommit(false);

            HashMap<String, String> columns = new HashMap<>();
            columns.put("id", id.toString());
            columns.put("deleted", "false");
            query = findByColumnsQuery(tableName, columns);

            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();

            if (rs.next()) {
                product = getProduct(rs);
            }
            connection.commit();
        } catch (SQLException ex) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                Logger.error(className,
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
        String query;

        try{
            connection.setAutoCommit(false);

            HashMap<String, String> columns = new HashMap<>();
            columns.put("supplier_id",id.toString());
            columns.put("deleted", "false");
            query = findByColumnsQuery(tableName, columns);

            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()){
                Products.add(getProduct(rs));
            }
            connection.commit();

        } catch (SQLException ex) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                Logger.error(className,
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
        String query;

        try {
            connection.setAutoCommit(false);

            HashMap<String, String> columns = new HashMap<>();
            columns.put("name", "%" + name + "%");
            columns.put("deleted", "false");
            query = findByColumnsQuery(tableName, columns);

            ps = connection.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {
                products.add(getProduct(rs));
            }

            connection.commit();

        } catch (SQLException ex) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                Logger.error(className,
                        "Error rolling back transaction: " + rollbackEx.getMessage());
            }
            throw new DAOException("Error while adding the Product: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeAll(connection, ps, rs);
        }
        return products;
    }

    @Override
    public ArrayList<Product> findAll() throws DAOException {

        ArrayList<Product> retailers = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;

        try {

            query = findAllQuery(tableName);

            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                retailers.add(getProduct(rs));
            }
        } catch (SQLException ex) {
            throw new DAOException("Error while fetching all Products: " + ex.getMessage(), ex);
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

        product.setId(UUID.fromString(rs.getString("id")));
        product.setSupplierId(UUID.fromString(rs.getString("supplier_id")));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setStockQuantity(rs.getLong("stock_quantity"));
        product.setPrice(rs.getDouble("price"));
        product.setDiscount(rs.getFloat("discount"));
        product.setImagePath(rs.getString("image_path"));

        return product;
    }
}
