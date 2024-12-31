package crud.base;

import crud.exception.MappingException;
import jakarta.servlet.http.HttpServletRequest;

public interface BaseMapper <TEntity extends BaseEntity> {
    public static final String MAPPER_ERROR_MESSAGE = "An error occured while mapping the object";

    public TEntity buildEntity(HttpServletRequest request) throws MappingException;

}
