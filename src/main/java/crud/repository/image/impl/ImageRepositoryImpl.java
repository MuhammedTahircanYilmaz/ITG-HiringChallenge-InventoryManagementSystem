package crud.repository.image.impl;

import crud.base.AbstractRepository;
import crud.exception.DAOException;
import crud.infrastructure.ConnectionFactory;
import crud.model.entities.Image;
import crud.repository.image.ImageRepository;
import crud.util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ImageRepositoryImpl extends AbstractRepository implements ImageRepository {

    protected ImageRepositoryImpl(){}

    private Connection connection;
    private static final String tableName = "images";
    private final String className = this.getClass().getName();

    public ImageRepositoryImpl(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        this.connection = connection;
    }

    @Override
    public Image add(Image entity) throws DAOException {

        PreparedStatement ps = null;

        String query = "INSERT INTO Images (Id, ImageLocation CreatedBy, CreatedAt, ProductId) VALUES (?, ?, ?, ?, ?)";

        try{
             ps = connection.prepareStatement(query);

             String imageId = entity.getId().toString();
             String productId = entity.getProductId();

            connection.setAutoCommit(false);

            ps.setString(1, imageId);
            ps.setString(2, entity.getImageLocation());
            ps.setString(3, entity.getCreatedBy());
            ps.setTimestamp(4, entity.getCreatedAt());
            ps.setString(5, productId);

            ps.executeUpdate();

            addImageToTheProductImages(imageId, productId);

            connection.commit();
            return entity;

        } catch (SQLException ex) {
            try {
                if (connection != null) {
                    connection.rollback();  // Rollback on error
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
    public void delete(Image entity) throws DAOException {

        if (entity == null) {
            throw new DAOException("Image cannot be null");
        }

        PreparedStatement ps = null;
        String query;
        try {
            connection.setAutoCommit(false);
            query = hardDeleteQuery(tableName, entity.getId().toString());

            ps = connection.prepareStatement(query);
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
                Logger.error(className,
                        "Error rolling back transaction: " + rollbackEx.getMessage());
            }
            throw new DAOException("Error while adding the Product: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnectionAndStatement(connection, ps);
        }
    }

    public ArrayList<Image> findAllByProductId(UUID id) throws DAOException{
        PreparedStatement ps = null;
        String query;
        try {
            HashMap<String, String> columns = new HashMap<>();
            columns.put("product_id", id.toString());
            columns.put("deleted", "false");
            query = findByColumnsQuery(tableName, columns);

            ps = connection.prepareStatement(query);
            connection.setAutoCommit(false);

            ArrayList<Image> images = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Image image = new Image();
                image.setId(UUID.fromString(rs.getString("id")));
                image.setImageLocation(rs.getString("image_location"));
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
            Logger.error(className,
                    "Error rolling back transaction: " + rollbackEx.getMessage());
        }
        throw new DAOException("Error while adding the Product: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnectionAndStatement(connection, ps);
        }
    }

    @Override
    public void addImageToTheProductImages(String imageId, String productId) throws DAOException {
        PreparedStatement ps = null;

        try{
            connection.setAutoCommit(false);
            ps = connection.prepareStatement("INSERT into ProductImages(ImageId, ProductId) VALUES (?, ?)");

            ps.setString(1, imageId);
            ps.setString(2, productId);
            ps.executeUpdate();

            connection.commit();

        } catch (SQLException ex){
            Logger.error(className, ex.getMessage());
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
    public List<Image> findAll() throws DAOException {
        return null;
    }
}
