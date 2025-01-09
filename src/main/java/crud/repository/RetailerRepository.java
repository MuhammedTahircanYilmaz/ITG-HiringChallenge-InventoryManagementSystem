package crud.repository;

import crud.base.BaseRepository;
import crud.exception.DAOException;
import crud.infrastructure.ConnectionFactory;
import crud.model.entities.Retailer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class RetailerRepository implements BaseRepository<Retailer> {

    protected RetailerRepository(){}

    private Connection connection;

    public RetailerRepository(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        this.connection = connection;
    }

    private static final String SQL_INSERT = "INSERT INTO Retailers (Id, Name, Email, Password, ImagePath, CreatedBy, CreatedAt, RoleName) VALUES (?,?,?,?,?,?,?,?)";
    private static final String SQL_UPDATE = "UPDATE Retailers SET Name = ?, Email = ?, Password = ?, ImagePath = ?, UpdatedBy = ?, UpdatedAt = ? WHERE id = ? AND Deleted = false";
    private static final String SQL_DELETE = "UPDATE Retailers SET Deleted = true WHERE id = ? AND Deleted = false";
    private static final String SQL_FIND_ALL = "SELECT * FROM Retailers AND Deleted = false";
    private static final String SQL_FIND_BY_NAME = "SELECT * FROM Retailers WHERE Name like ? AND Deleted = false";
    private static final String SQL_FIND_BY_EMAIL = "SELECT * FROM Retailers WHERE Email like ? AND Deleted = false";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM Retailers WHERE id = ? AND Deleted = false";

    @Override
    public Retailer add(Retailer entity) throws DAOException {
        if (entity == null) {
            throw new DAOException("Retailer entity cannot be null");
        }


        PreparedStatement ps = null;
        try {
            if (connection.isClosed()) {
                throw new DAOException("Database connection is not established.");
            }
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
            return entity;

        } catch (SQLException ex) {
            throw new DAOException("Error while adding the Retailer: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnectionAndStatement(connection, ps);
        }
    }

    @Override
    public Retailer update(Retailer entity) throws DAOException {
        if (entity == null) {
            throw new DAOException("Retailer entity cannot be null");
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
            ps.setString(7, entity.getId().toString());

            ps.executeUpdate();
            connection.commit();

        } catch (SQLException ex) {
            throw new DAOException("Error while updating the Retailer: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnectionAndStatement(connection, ps);
        }
        return entity;
    }

    @Override
    public void delete(Retailer retailer) throws DAOException {
        if (retailer == null) {
            throw new DAOException("Retailer cannot be null");
        }

        PreparedStatement ps = null;
        try {
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(SQL_DELETE);

            ps.setString(1, retailer.getDeletedBy());
            ps.setTimestamp(2, retailer.getDeletedAt());
            ps.setString(3, retailer.getId().toString());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("No Retailer found with ID: " + retailer.getId());
            }
            connection.commit();

        } catch (SQLException ex) {
            throw new DAOException("Error while deleting the Retailer: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnectionAndStatement(connection, ps);
        }
    }

    @Override
    public Retailer findById(UUID id) throws DAOException {
        if (id == null) {
            throw new DAOException("Retailer ID cannot be null");
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        Retailer retailer = null;

        try {
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(SQL_FIND_BY_ID);
            ps.setString(1, id.toString());
            rs = ps.executeQuery();

            if (rs.next()) {
                retailer = getRetailer(rs);
            }
            connection.commit();

        } catch (SQLException ex) {
            throw new DAOException("Error while finding the Retailer: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeAll(connection, ps, rs);
        }
        return retailer;
    }

    public Retailer findByEmail(String email) throws DAOException{

        if (email == null) {
            throw new DAOException("Retailer email cannot be null");
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        Retailer retailer = null;

        try{
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(SQL_FIND_BY_EMAIL);
            ps.setString(1,email);

            rs = ps.executeQuery();

            retailer = getRetailer(rs);

            // logger koy buraya
            connection.commit();

        } catch(SQLException ex){
            // logger.error("The Retailer could not be found.", ex);
            throw new DAOException(ex);
        } finally {
            ConnectionFactory.closeAll(connection, ps, rs);
        }

        return retailer;
    }

    public ArrayList<Retailer> findAllByName(String name) throws DAOException{

        if (name == null) {
            throw new DAOException("Retailer name cannot be null");
        }
        ArrayList<Retailer> retailers = new ArrayList<Retailer>();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(SQL_FIND_BY_NAME);

            ps.setString(1,"%"+name);
            rs = ps.executeQuery();

            while (rs.next()) {
                retailers.add(getRetailer(rs));
            }
            connection.commit();

        } catch (SQLException ex) {
            throw new DAOException(ex);
        } finally {
            ConnectionFactory.closeAll(connection,ps,rs);
        }

        return retailers;
    }

    @Override
    public ArrayList<Retailer> getAll() throws DAOException {
        ArrayList<Retailer> retailers = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(SQL_FIND_ALL);
            rs = ps.executeQuery();

            while (rs.next()) {
                retailers.add(getRetailer(rs));
            }
            connection.commit();
        } catch (SQLException ex) {
            throw new DAOException("Error while fetching all Retailers: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeAll(connection, ps, rs);
        }
        return retailers;
    }

    private Retailer getRetailer(ResultSet rs) throws SQLException {
        if (rs == null) {
            throw new SQLException("ResultSet cannot be null");
        }

        Retailer retailer = new Retailer();

        retailer.setId(UUID.fromString(rs.getString("Id")));
        retailer.setName(rs.getString("Name"));
        retailer.setEmail(rs.getString("Email"));
        retailer.setPassword(rs.getString("Password"));
        retailer.setImageLocation(rs.getString("ImagePath"));

        return retailer;
    }
}
