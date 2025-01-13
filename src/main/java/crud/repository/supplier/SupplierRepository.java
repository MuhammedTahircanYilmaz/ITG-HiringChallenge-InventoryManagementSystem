package crud.repository.supplier;

import crud.base.BaseRepository;
import crud.exception.DAOException;
import crud.model.entities.Supplier;

import java.util.ArrayList;

public interface SupplierRepository extends BaseRepository<Supplier> {

    public ArrayList<Supplier> findAllByName(String name) throws DAOException;
    public Supplier findByEmail(String email) throws DAOException;
}