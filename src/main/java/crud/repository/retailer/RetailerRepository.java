package crud.repository.retailer;

import crud.base.BaseRepository;
import crud.exception.DAOException;
import crud.model.entities.Retailer;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface RetailerRepository extends BaseRepository<Retailer> {

    public ArrayList<Retailer> findAllByName(String name) throws DAOException;
    public Retailer findByEmail(String email) throws DAOException;
}
