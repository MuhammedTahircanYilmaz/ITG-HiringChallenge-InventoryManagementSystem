package crud.base;

import crud.exception.BusinessException;
import crud.exception.DAOException;
import crud.exception.NotFoundException;

import java.util.UUID;

public class BaseBusinessRules<TEntity extends BaseEntity, TRepository extends BaseRepository> {
    private TRepository repository;
    public BaseBusinessRules(TRepository repository) { this.repository = repository;}
    public boolean entityExists(UUID id) {

        try {
            TEntity entity = (TEntity) repository.findById(id);
            if (entity == null) {
                throw new BusinessException("The entity does not exist");
            }
            return true;
        } catch (DAOException ex){
            throw new NotFoundException(ex.getMessage());
        }
    }
}
