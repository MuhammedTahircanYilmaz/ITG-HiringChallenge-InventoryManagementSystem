package crud.service.suppliers.queries;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.base.ServiceResult;
import crud.dtos.suppliers.responses.SupplierResponseDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.SupplierMapper;
import crud.model.entities.Supplier;
import crud.repository.supplier.SupplierRepositoryImpl;
import crud.service.suppliers.rules.SupplierBusinessRules;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class GetByIdSupplierQuery extends AbstractCommand {

    private final SupplierRepositoryImpl repository;
    private final SupplierBusinessRules rules;
    private final AuthService authService ;
    private final SupplierMapper mapper;
    private String page = PROFILE;

    public GetByIdSupplierQuery(SupplierRepositoryImpl repository, SupplierBusinessRules rules, AuthService authService, SupplierMapper mapper) {
        this.repository = repository;
        this.authService = authService;
        this.mapper = mapper;
        this.rules = rules;
    }

    @Override
    public ServiceResult execute(HttpServletRequest request) {
        try{
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            rules.entityExists(UUID.fromString(request.getParameter("id")));

            Supplier Supplier = repository.findById(UUID.fromString(request.getParameter("supplierId")));
            SupplierResponseDto dto = mapper.mapEntityToEntityResponseDto(Supplier);

            return createJsonResponse(dto);

        } catch (DAOException | MappingException ex){

            Logger.error(this.getClass().getName(), ex.getMessage());
            page = PROFILE;

            setException(request, ex);
        }
        return createView( page);
    }
}
