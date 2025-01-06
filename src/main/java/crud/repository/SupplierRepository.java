package crud.repository;

import crud.base.BaseRepository;
import crud.exception.DAOException;
import crud.infrastructure.ConnectionFactory;
import crud.model.entities.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class SupplierRepository implements BaseRepository<Supplier> {

    private final Connection connection;

    private static final String SQL_INSERT = "INSERT INTO Suppliers (Id, Name, Email, Password, ImagePath, CreatedBy, CreatedAt, RoleName) VALUES (?,?,?,?,?,?,?,?)";
    private static final String SQL_UPDATE = "UPDATE Suppliers SET Name = ?, Email = ?, Password = ?, ImagePath = ?, UpdatedBy = ?, UpdatedAt = ? WHERE id = ? AND Deleted = false";
    private static final String SQL_DELETE = "UPDATE Suppliers SET Deleted = true WHERE id = ? AND Deleted = false";
    private static final String SQL_FIND_ALL = "SELECT * FROM Suppliers AND Deleted = false";
    private static final String SQL_FIND_BY_NAME = "SELECT * FROM Suppliers WHERE Name like ? AND Deleted = false";
    private static final String SQL_FIND_BY_EMAIL = "SELECT * FROM Suppliers WHERE Email like ? AND Deleted = false";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM Suppliers WHERE id = ? AND Deleted = false";


    public SupplierRepository(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        this.connection = connection;
    }

    @Override
    public Supplier add(Supplier entity) throws DAOException {
        if (entity == null) {
            throw new DAOException("Supplier entity cannot be null");
        }

        PreparedStatement ps = null;
        try {
            connection.setAutoCommit(false);

            ps = connection.prepareStatement(SQL_INSERT);

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

        } catch (SQLException ex) {
            throw new DAOException("Error while adding the Supplier: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnectionAndStatement(connection, ps);
        }
        return entity;
    }

    @Override
    public Supplier update(Supplier entity) throws DAOException {
        if (entity == null) {
            throw new DAOException("Supplier entity cannot be null");
        }

        PreparedStatement ps = null;
        try {
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(SQL_UPDATE);

            ps.setString(1, entity.getName());
            ps.setString(2, entity.getEmail());
            ps.setString(3, entity.getPassword());
            ps.setString(4, entity.getImageLocation());
            ps.setString(5, entity.getUpdatedBy());
            ps.setTimestamp(6, entity.getUpdatedAt());
            ps.setString(5, entity.getId().toString());

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
    public void delete(Supplier supplier) throws DAOException {
        if (supplier == null) {
            throw new DAOException("Supplier cannot be null");
        }

        PreparedStatement ps = null;
        try {
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(SQL_DELETE);
            ps.setString(1, supplier.getDeletedBy());
            ps.setTimestamp(2, supplier.getDeletedAt());
            ps.setString(3, supplier.getId().toString());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("No Supplier found with ID: " + supplier.getId());
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

        try {
            connection.setAutoCommit(false);

            ps = connection.prepareStatement(SQL_FIND_BY_ID);
            ps.setString(1, id.toString());
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

        try{
            connection.setAutoCommit(false);

            ps = connection.prepareStatement(SQL_FIND_BY_EMAIL);
            ps.setString(1,email);

            rs = ps.executeQuery();

            supplier = getSupplier(rs);

            // logger koy buraya
            connection.commit();

        } catch(SQLException ex){
            // logger.error("The Supplier could not be found.", ex);
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

        try {
            connection.setAutoCommit(false);

            ps = connection.prepareStatement(SQL_FIND_BY_NAME);

            ps.setString(1,"%"+name);
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
    public ArrayList<Supplier> getAll() throws DAOException {
        ArrayList<Supplier> suppliers = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection.setAutoCommit(false);

            ps = connection.prepareStatement(SQL_FIND_ALL);
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
        supplier.setName(rs.getString("Name"));
        supplier.setEmail(rs.getString("Email"));
        supplier.setPassword(rs.getString("Password"));
        supplier.setImageLocation(rs.getString("ImagePath"));

        return supplier;
    }
}
