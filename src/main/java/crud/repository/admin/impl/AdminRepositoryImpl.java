package crud.repository.admin.impl;

import crud.exception.DAOException;
import crud.infrastructure.ConnectionFactory;
import crud.model.entities.Retailer;
import crud.model.entities.User;
import crud.util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AdminRepositoryImpl {

    protected AdminRepositoryImpl(){}

    private Connection connection;

    public AdminRepositoryImpl(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        this.connection = connection;
    }

    public User findById(UUID id) throws DAOException {
        if (id == null) {
            throw new DAOException("Admin ID cannot be null");
        }
        String query = "SELECT * FROM Admins WHERE id = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        User admin = null;

        try {
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(query);
            ps.setString(1, id.toString());
            rs = ps.executeQuery();

            if (rs.next()) {
                admin = getAdmin(rs);
            }
            connection.commit();
        } catch (SQLException ex) {
            throw new DAOException("Error while finding the Admin: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeAll(connection, ps, rs);
        }
        return admin;
    }

    public User findByEmail(String email) throws DAOException{

        if (email == null) {
            throw new DAOException("Retailer email cannot be null");
        }
        String query = "SELECT * FROM Admins WHERE Email like ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        User admin = null;

        try{
            ps = connection.prepareStatement(query);
            connection.setAutoCommit(false);
            ps.setString(1,email);

            rs = ps.executeQuery();

            admin = getAdmin(rs);

            connection.commit();
        } catch(SQLException ex){
            Logger.error(this.getClass().getName(), "The Retailer could not be found.");
            throw new DAOException(ex);
        } finally {
            ConnectionFactory.closeAll(connection, ps, rs);
        }

        return admin;
    }

    private User getAdmin(ResultSet rs) throws SQLException {
        if (rs == null) {
            throw new SQLException("ResultSet cannot be null");
        }

        User admin = new Retailer();

        admin.setId(UUID.fromString(rs.getString("Id")));
        admin.setName(rs.getString("Name"));
        admin.setEmail(rs.getString("Email"));
        admin.setPassword(rs.getString("Password"));


        return admin;
    }
}
