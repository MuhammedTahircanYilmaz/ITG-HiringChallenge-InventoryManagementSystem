package crud.repository.product;

import crud.base.BaseRepository;
import crud.exception.DAOException;
import crud.model.entities.Product;

import java.util.ArrayList;
import java.util.UUID;

public interface ProductRepository extends BaseRepository<Product> {
    ArrayList<Product> findBySupplierId(UUID id) throws DAOException;
    ArrayList<Product> findAllByName(String name) throws DAOException;
}
