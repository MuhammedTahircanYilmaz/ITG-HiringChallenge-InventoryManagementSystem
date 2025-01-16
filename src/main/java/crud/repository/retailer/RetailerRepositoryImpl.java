package crud.repository.retailer;

import crud.base.AbstractRepository;
import crud.base.BaseRepository;
import crud.exception.DAOException;
import crud.infrastructure.ConnectionFactory;
import crud.model.entities.Retailer;
import crud.util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class RetailerRepositoryImpl extends AbstractRepository implements RetailerRepository {

    protected RetailerRepositoryImpl(){}

    private Connection connection;

    public RetailerRepositoryImpl(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        this.connection = connection;
    }

    private final String tableName = "retailers";
    private final String className = this.getClass().getName();

    @Override
    public Retailer add(Retailer entity) throws DAOException {
        if (entity == null) {
            throw new DAOException("Retailer entity cannot be null");
        }


        PreparedStatement ps = null;
        String query = "INSERT INTO retailers (id, name, email, password, image_path, created_by, created_at, role_name) VALUES (?,?,?,?,?,?,?,?)";
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
        String query = "UPDATE retailers SET name = ?, email = ?, password = ?, image_path = ?, updated_by = ?, updated_at = ? WHERE id = ? AND deleted = false";

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

        String query;
        PreparedStatement ps = null;
        try {
            connection.setAutoCommit(false);
            query = softDeleteQuery(tableName, retailer.getId().toString());
            ps = connection.prepareStatement(query);

            ps.setString(1, retailer.getDeletedBy());
            ps.setTimestamp(2, retailer.getDeletedAt());

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
        Retailer retailer;
        String query;
        try{
            connection.setAutoCommit(false);


            HashMap<String, String > columns = new HashMap<>();

            columns.put("email", email);
            columns.put("deleted", "false");

            query = findByColumnsQuery(tableName,columns);

            ps = connection.prepareStatement(query);

            rs = ps.executeQuery();

            retailer = getRetailer(rs);

            connection.commit();

        } catch(SQLException ex){
            Logger.error(className, ex.getMessage());
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
    public ArrayList<Retailer> findAll() throws DAOException {
        ArrayList<Retailer> retailers = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query ;

        try {
            connection.setAutoCommit(false);
            query = findAllQuery(tableName);
            ps = connection.prepareStatement(query);

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

        retailer.setId(UUID.fromString(rs.getString("id")));
        retailer.setName(rs.getString("name"));
        retailer.setEmail(rs.getString("email"));
        retailer.setPassword(rs.getString("password"));
        retailer.setImageLocation(rs.getString("image_path"));

        return retailer;
    }
}
