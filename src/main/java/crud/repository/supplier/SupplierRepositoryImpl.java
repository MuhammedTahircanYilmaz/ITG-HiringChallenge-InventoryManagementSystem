package crud.repository.supplier;

import crud.base.AbstractRepository;
import crud.exception.DAOException;
import crud.infrastructure.ConnectionFactory;
import crud.model.entities.Supplier;
import crud.util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class SupplierRepositoryImpl extends AbstractRepository implements SupplierRepository {

    protected SupplierRepositoryImpl(){}

    private Connection connection;

    public SupplierRepositoryImpl(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        this.connection = connection;
    }

    private final String tableName = "suppliers";
    private final String className = this.getClass().getName();

    @Override
    public Supplier add(Supplier entity) throws DAOException {
        if (entity == null) {
            throw new DAOException("Supplier entity cannot be null");
        }


        PreparedStatement ps = null;
        String query = "INSERT INTO suppliers (id, name, email, password, image_path, created_by, created_at, role_name) VALUES (?,?,?,?,?,?,?,?)";
        try {
            if (connection.isClosed()) {
                throw new DAOException("Database connection is not established.");
            }
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(query);


            ps.setString(1, entity.getId().toString());
            ps.setString(2, entity.getName());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getPassword());
            ps.setString(5, entity.getImageLocation());
            ps.setString(6, entity.getId().toString());
            ps.setTimestamp(7, entity.getCreatedAt());
            ps.setString(8, entity.getRoleName().toString());

            ps.executeUpdate();
            connection.commit();
            return entity;

        } catch (SQLException ex) {
            throw new DAOException("Error while adding the Supplier: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnectionAndStatement(connection, ps);
        }
    }

    @Override
    public Supplier update(Supplier entity) throws DAOException {
        if (entity == null) {
            throw new DAOException("Supplier entity cannot be null");
        }

        PreparedStatement ps = null;
        String query = "UPDATE suppliers SET name = ?, email = ?, password = ?, image_path = ?, updated_by = ?, updated_at = ? WHERE id = ? AND deleted = false";

        try {
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(query);

            ps.setString(1, entity.getName());
            ps.setString(2, entity.getEmail());
            ps.setString(3, entity.getPassword());
            ps.setString(4, entity.getImageLocation());
            ps.setString(5, entity.getUpdatedBy());
            ps.setTimestamp(6, entity.getUpdatedAt());
            ps.setString(7, entity.getId().toString());

            ps.executeUpdate();
            connection.commit();

        } catch (SQLException ex) {
            throw new DAOException("Error while updating the Supplier: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnectionAndStatement(connection, ps);
        }
        return entity;
    }

    @Override
    public void delete(Supplier Supplier) throws DAOException {
        if (Supplier == null) {
            throw new DAOException("Supplier cannot be null");
        }

        String query;
        PreparedStatement ps = null;
        try {
            connection.setAutoCommit(false);
            query = softDeleteQuery(tableName, Supplier.getId().toString());
            ps = connection.prepareStatement(query);

            ps.setString(1, Supplier.getDeletedBy());
            ps.setTimestamp(2, Supplier.getDeletedAt());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("No Supplier found with ID: " + Supplier.getId());
            }
            connection.commit();

        } catch (SQLException ex) {
            throw new DAOException("Error while deleting the Supplier: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnectionAndStatement(connection, ps);
        }
    }

    @Override
    public Supplier findById(UUID id) throws DAOException {
        if (id == null) {
            throw new DAOException("Supplier ID cannot be null");
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        Supplier supplier = null;
        String query;
        try {
            connection.setAutoCommit(false);

            HashMap<String, String > columns = new HashMap<>();

            columns.put("id", id.toString());
            columns.put("deleted", "false");

            query = findByColumnsQuery(tableName,columns);

            ps = connection.prepareStatement(query);

            rs = ps.executeQuery();

            if (rs.next()) {
                supplier = getSupplier(rs);
            }
            connection.commit();

        } catch (SQLException ex) {
            throw new DAOException("Error while finding the Supplier: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeAll(connection, ps, rs);
        }
        return supplier;
    }

    public Supplier findByEmail(String email) throws DAOException{

        if (email == null) {
            throw new DAOException("Supplier email cannot be null");
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        Supplier supplier = null;
        String query;
        try{
            connection.setAutoCommit(false);


            HashMap<String, String > columns = new HashMap<>();

            columns.put("email", email);
            columns.put("deleted", "false");

            query = findByColumnsQuery(tableName,columns);

            ps = connection.prepareStatement(query);

            rs = ps.executeQuery();

            supplier = getSupplier(rs);

            connection.commit();

        } catch(SQLException ex){
            Logger.error(className, ex.getMessage());
            throw new DAOException(ex);
        } finally {
            ConnectionFactory.closeAll(connection, ps, rs);
        }

        return supplier;
    }

    public ArrayList<Supplier> findAllByName(String name) throws DAOException{

        if (name == null) {
            throw new DAOException("Supplier name cannot be null");
        }
        ArrayList<Supplier> suppliers = new ArrayList<Supplier>();

        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        try {
            connection.setAutoCommit(false);

            HashMap<String, String > columns = new HashMap<>();

            columns.put("name", "%"+name+"%");
            columns.put("deleted", "false");

            query = findByColumnsQuery(tableName,columns);
            ps = connection.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {
                suppliers.add(getSupplier(rs));
            }
            connection.commit();

        } catch (SQLException ex) {
            throw new DAOException(ex);
        } finally {
            ConnectionFactory.closeAll(connection,ps,rs);
        }

        return suppliers;
    }

    @Override
    public ArrayList<Supplier> findAll() throws DAOException {
        ArrayList<Supplier> suppliers = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query ;

        try {
            connection.setAutoCommit(false);
            query = findAllQuery(tableName);
            ps = connection.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {
                suppliers.add(getSupplier(rs));
            }
            connection.commit();
        } catch (SQLException ex) {
            throw new DAOException("Error while fetching all Suppliers: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeAll(connection, ps, rs);
        }
        return suppliers;
    }

    private Supplier getSupplier(ResultSet rs) throws SQLException {
        if (rs == null) {
            throw new SQLException("ResultSet cannot be null");
        }

        Supplier supplier = new Supplier();

        supplier.setId(UUID.fromString(rs.getString("Id")));
        supplier.setName(rs.getString("name"));
        supplier.setEmail(rs.getString("email"));
        supplier.setPassword(rs.getString("password"));
        supplier.setImageLocation(rs.getString("image_path"));

        return supplier;
    }
}
