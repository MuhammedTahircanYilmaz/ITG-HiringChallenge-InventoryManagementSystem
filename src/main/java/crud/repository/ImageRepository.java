package crud.repository;

import crud.base.BaseRepository;
import crud.exception.DAOException;
import crud.infrastructure.ConnectionFactory;
import crud.model.entities.Image;
import crud.util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ImageRepository implements BaseRepository<Image> {
    private static final String SQL_INSERT = "INSERT INTO Images (Id, ImageLocation CreatedBy, CreatedAt, ProductId) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_FIND_ALL_BY_PRODUCT_ID = "SELECT i.Id, i.ImageLocation FROM Images i " +
                                      "JOIN ProductImages pi ON i.Id = pi.ImageId WHERE p.ProductId = ? ";
    private static final String SQL_DELETE = "DELETE FROM Images WHERE Id = ?";

    protected ImageRepository(){}

    private Connection connection;

    public ImageRepository(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        this.connection = connection;
    }

    @Override
    public Image add(Image entity) throws DAOException {
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        try{
             ps = connection.prepareStatement(SQL_INSERT);
             ps2 = connection.prepareStatement("INSERT into ProductImages(ImageId, ProductId) VALUES (?, ?)");
            connection.setAutoCommit(false);

            ps.setString(1, entity.getId().toString());
            ps.setString(2, entity.getImageLocation());
            ps.setString(3, entity.getCreatedBy());
            ps.setTimestamp(4, entity.getCreatedAt());
            ps.setString(5, entity.getProductId());

            ps.executeUpdate();

            ps2.setString(1, entity.getId().toString());
            ps2.setString(2, entity.getProductId());
            ps2.executeUpdate();

            connection.commit();
            return entity;

        } catch (SQLException ex) {
            try {
                if (connection != null) {
                    connection.rollback();  // Rollback on error
                }
            } catch (SQLException rollbackEx) {
                Logger.error(this.getClass().getName(),
                        "Error rolling back transaction: " + rollbackEx.getMessage());
            }
            throw new DAOException("Error while adding the Product: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnectionAndStatement(connection, ps);
        }
    }


    @Override
    public void delete(Image entity) throws DAOException {

        if (entity == null) {
            throw new DAOException("Image cannot be null");
        }

        PreparedStatement ps = null;

        try {
            connection.setAutoCommit(false);

            ps = connection.prepareStatement(SQL_DELETE);
            ps.setString(1,entity.getId().toString());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("No Image found with ID: " + entity.getId());
            }
            connection.commit();
        } catch (SQLException ex) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                Logger.error(this.getClass().getName(),
                        "Error rolling back transaction: " + rollbackEx.getMessage());
            }
            throw new DAOException("Error while adding the Product: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnectionAndStatement(connection, ps);
        }
    }

    public ArrayList<Image> findAllByProductId(UUID id) throws DAOException{
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(SQL_FIND_ALL_BY_PRODUCT_ID);
            connection.setAutoCommit(false);

            ps.setString(1, id.toString());

            ArrayList<Image> images = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Image image = new Image();
                image.setId(UUID.fromString(rs.getString("Id")));
                image.setImageLocation(rs.getString("ImageLocation"));
                images.add(image);
            }
            connection.commit();
            return images;

        } catch (SQLException ex) {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException rollbackEx) {
            Logger.error(this.getClass().getName(),
                    "Error rolling back transaction: " + rollbackEx.getMessage());
        }
        throw new DAOException("Error while adding the Product: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnectionAndStatement(connection, ps);
        }
    }

    @Override
    public Image update(Image entity) throws DAOException {
        return null;
    }

    @Override
    public Image findById(UUID id) throws DAOException {
        return null;
    }

    @Override
    public List<Image> getAll() throws DAOException {
        return null;
    }
}
