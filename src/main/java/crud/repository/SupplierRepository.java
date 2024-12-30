package crud.repository;

import crud.base.BaseRepository;
import crud.exception.DAOException;
import crud.infrastructure.ConnectionFactory;
import crud.model.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class SupplierRepository implements BaseRepository<Supplier, UUID> {

    private final Connection connection;

    private static final String SQL_INSERT = "INSERT INTO Suppliers (Id, Name, Email, Password, ImagePath) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE Suppliers SET Name = ?, Email = ?, Password = ?, ImagePath = ? WHERE Id = ?";
    private static final String SQL_DELETE = "DELETE FROM Suppliers WHERE Id = ?";
    private static final String SQL_FIND_ALL = "SELECT * FROM Suppliers";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM Suppliers WHERE Id = ?";
    private static final String SQL_FIND_BY_NAME = "SELECT * FROM Suppliers WHERE Name like ?";
    private static final String SQL_FIND_BY_EMAIL = "SELECT * FROM Suppliers WHERE Email like ?";

    public SupplierRepository(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        this.connection = connection;
    }

    @Override
    public void add(Supplier entity) throws DAOException {
        if (entity == null) {
            throw new DAOException("Supplier entity cannot be null");
        }

        PreparedStatement ps = null;
        try {
            UUID id = UUID.randomUUID();
            ps = connection.prepareStatement(SQL_INSERT);

            ps.setString(1, id.toString());
            ps.setString(2, entity.getName());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getPassword());
            ps.setString(5, entity.getImageLocation());

            ps.executeUpdate();
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
        try {
            ps = connection.prepareStatement(SQL_UPDATE);

            ps.setString(1, entity.getName());
            ps.setString(2, entity.getEmail());
            ps.setString(3, entity.getPassword());
            ps.setString(4, entity.getImageLocation());
            ps.setString(5, entity.getId().toString());

            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DAOException("Error while updating the Supplier: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnectionAndStatement(connection, ps);
        }
        return entity;
    }

    @Override
    public void delete(UUID id) throws DAOException {
        if (id == null) {
            throw new DAOException("Supplier ID cannot be null");
        }

        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(SQL_DELETE);
            ps.setString(1, id.toString());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("No Supplier found with ID: " + id);
            }
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
            ps = connection.prepareStatement(SQL_FIND_BY_ID);
            ps.setString(1, id.toString());
            rs = ps.executeQuery();

            if (rs.next()) {
                supplier = getSupplier(rs);
            }
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
            ps = connection.prepareStatement(SQL_FIND_BY_EMAIL);
            ps.setString(1,email);

            rs = ps.executeQuery();

            supplier = getSupplier(rs);

            // logger koy buraya

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
            ps = connection.prepareStatement(SQL_FIND_BY_NAME);

            ps.setString(1,"%"+name);
            rs = ps.executeQuery();

            while (rs.next()) {
                suppliers.add(getSupplier(rs));
            }

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
            ps = connection.prepareStatement(SQL_FIND_ALL);
            rs = ps.executeQuery();

            while (rs.next()) {
                suppliers.add(getSupplier(rs));
            }
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