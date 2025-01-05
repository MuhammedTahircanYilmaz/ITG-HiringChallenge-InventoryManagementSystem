package crud.base;

import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public abstract class BaseValidator
        <TEntity extends BaseEntity,
        TRules extends BaseBusinessRules<TEntity, TRepository>,
        TRepository extends BaseRepository<TEntity>> {

    TRules rules;
    public BaseValidator(TRules rules){
        this.rules = rules;
    }

    public abstract boolean validateAddRequest(HttpServletRequest request);
    public abstract boolean validateUpdateRequest(HttpServletRequest request);

    public boolean validateDeleteRequest(HttpServletRequest request){
        UUID id = UUID.fromString(request.getParameter("id"));
        return rules.entityExists(id);
    }

    public boolean validateGetByIdRequest(HttpServletRequest request){
        UUID id = UUID.fromString(request.getParameter("id"));
        return rules.entityExists(id);
    }

}
