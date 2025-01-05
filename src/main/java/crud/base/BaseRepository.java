package crud.base;

import crud.exception.DAOException;
import java.util.List;
import java.util.UUID;

public interface BaseRepository<TEntity extends BaseEntity>{

    TEntity add(TEntity entity) throws DAOException;
    TEntity update(TEntity entity) throws DAOException;
    void delete(TEntity entity) throws DAOException;
    TEntity findById(UUID id) throws DAOException;
    List<TEntity> getAll() throws DAOException;
}
