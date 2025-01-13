package crud.repository.image;

import crud.base.BaseRepository;
import crud.exception.DAOException;
import crud.model.entities.Image;

import java.util.ArrayList;
import java.util.UUID;

public interface ImageRepository extends BaseRepository<Image> {

    public ArrayList<Image> findAllByProductId(UUID id) throws DAOException;
    public void addImageToTheProductImages(String imageId, String ProductId) throws DAOException;
}
