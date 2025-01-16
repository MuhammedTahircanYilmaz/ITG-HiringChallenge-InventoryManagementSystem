package crud.repository;

import crud.exception.BusinessException;
import crud.exception.DAOException;
import crud.infrastructure.ConnectionFactory;
import crud.model.entities.Token;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TokenRepositoryImpl {
    private final Connection connection;


    public TokenRepositoryImpl(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        this.connection = connection;
    }

    public Token add(Token token) throws DAOException {
        if (token == null) {
            throw new DAOException("Token entity cannot be null");
        }

        PreparedStatement ps = null;
        try {
            connection.setAutoCommit(false);

            String query = "INSERT INTO tokens (id, user_id, token, expiry) VALUES (?, ?, ?, ?)";

            ps = connection.prepareStatement(query);
            ps.setString(1, UUID.randomUUID().toString());
            ps.setString(2, token.getUserId().toString());
            ps.setString(3, token.getToken());
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(token.getExpiry()));

            ps.executeUpdate();
            connection.commit();

        } catch (SQLException ex) {
            throw new DAOException("Error while adding the token: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnectionAndStatement(connection, ps);
        }

        return token;
    }

    public void delete(String token) throws DAOException {
        if (token == null) {
            throw new DAOException("Token cannot be null");
        }

        PreparedStatement ps = null;
        try {
            connection.setAutoCommit(false);
            String query = "DELETE FROM tokens WHERE token = ?";

            ps = connection.prepareStatement(query);
            ps.setString(1, token);

            ps.executeUpdate();
            connection.commit();

        } catch (SQLException ex) {
            throw new DAOException("Error while deleting the token: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnectionAndStatement(connection, ps);
        }
    }

    public Token findByUserId(UUID userId) throws DAOException {
        if (userId == null) {
            throw new DAOException("User ID cannot be null");
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        Token token = null;

        try {
            connection.setAutoCommit(false);

            String query = "SELECT * FROM tokens WHERE user_id = ?";

            ps = connection.prepareStatement(query);
            ps.setString(1, userId.toString());

            rs = ps.executeQuery();

            if (rs.next()) {
                token = getToken(rs);
            }

            if (token.getExpiry().isBefore(java.time.LocalDateTime.now()) ) {
                delete(token.getToken());
                throw new BusinessException("Your session has expired, please login again.");
            }

            connection.commit();

        } catch (SQLException ex) {
            throw new DAOException("Error while finding the token by user ID: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeAll(connection, ps, rs);
        }

        return token;
    }

    public Token findByToken(String tokenValue) throws DAOException {
        if (tokenValue == null) {
            throw new DAOException("Token cannot be null");
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        Token token = null;

        try {
            connection.setAutoCommit(false);

            String query = "SELECT * FROM tokens WHERE token = ?";

            ps = connection.prepareStatement(query);
            ps.setString(1, tokenValue);
            rs = ps.executeQuery();

            if (rs.next()) {
                token = getToken(rs);
            }
            connection.commit();

        } catch (SQLException ex) {
            throw new DAOException("Error while finding the token: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeAll(connection, ps, rs);
        }

        return token;
    }

    public void deleteExpiredTokens() throws DAOException {
        PreparedStatement ps = null;
        try {
            connection.setAutoCommit(false);

            String query = "DELETE FROM tokens WHERE expiry < CURRENT_TIMESTAMP";

            ps = connection.prepareStatement(query);

            ps.executeUpdate();
            connection.commit();

        } catch (SQLException ex) {
            throw new DAOException("Error while deleting expired tokens: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnectionAndStatement(connection, ps);
        }
    }

    private Token getToken(ResultSet rs) throws SQLException {
        if (rs == null) {
            throw new SQLException("ResultSet cannot be null");
        }

        Token token = new Token();
        token.setId(UUID.fromString(rs.getString("Id")));
        token.setUserId(UUID.fromString(rs.getString("UserId")));
        token.setToken(rs.getString("Token"));
        token.setExpiry(rs.getTimestamp("Expiry").toLocalDateTime());

        return token;
    }
}