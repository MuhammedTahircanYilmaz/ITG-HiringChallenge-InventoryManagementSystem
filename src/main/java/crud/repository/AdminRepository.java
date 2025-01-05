package crud.repository;

import crud.base.BaseRepository;
import crud.exception.DAOException;
import crud.infrastructure.ConnectionFactory;
import crud.model.entities.Retailer;
import crud.model.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class AdminRepository {

    protected AdminRepository(){}

    private Connection connection;

    public AdminRepository(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        this.connection = connection;
    }

    private static final String SQL_FIND_BY_EMAIL = "SELECT * FROM Admins WHERE Email like ?";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM Admins WHERE id = ?";


    public Retailer findById(UUID id) throws DAOException {
        if (id == null) {
            throw new DAOException("Retailer ID cannot be null");
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        Retailer retailer = null;

        try {
            ps = connection.prepareStatement(SQL_FIND_BY_ID);
            ps.setString(1, id.toString());
            rs = ps.executeQuery();

            if (rs.next()) {
                retailer = getRetailer(rs);
            }
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
            ps = connection.prepareStatement(SQL_FIND_BY_EMAIL);
            ps.setString(1,email);

            rs = ps.executeQuery();

            retailer = getRetailer(rs);

            // logger koy buraya

        } catch(SQLException ex){
            // logger.error("The Retailer could not be found.", ex);
            throw new DAOException(ex);
        } finally {
            ConnectionFactory.closeAll(connection, ps, rs);
        }

        return retailer;
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
