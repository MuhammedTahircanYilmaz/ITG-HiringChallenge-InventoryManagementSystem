package crud.base;

import crud.exception.DAOException;
import crud.model.Retailer;

import java.util.ArrayList;

public interface BaseRepository<TEntity extends BaseEntity, TId>{

    public void add(TEntity entity) throws DAOException;
    public TEntity update(TEntity entity) throws DAOException;
    public void delete(TId id) throws DAOException;
    public TEntity findById(TId id) throws DAOException;
    public ArrayList<TEntity> getAll() throws DAOException;
}
