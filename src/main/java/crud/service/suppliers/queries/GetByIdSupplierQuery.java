package crud.service.suppliers.queries;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.dtos.suppliers.responses.SupplierResponseDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.SupplierMapper;
import crud.model.entities.Supplier;
import crud.model.entities.Supplier;
import crud.repository.SupplierRepository;
import crud.service.validation.SupplierValidator;
import crud.util.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class GetByIdSupplierQuery extends AbstractCommand {

    private final SupplierRepository repository;
    private final SupplierValidator validator;
    private final AuthService authService ;
    private final SupplierMapper mapper;
    private String page = PAGE_SUPPLIER_FORM;

    public GetByIdSupplierQuery(SupplierRepository repository, SupplierValidator validator, AuthService authService, SupplierMapper mapper) {
        this.repository = repository;
        this.authService = authService;
        this.mapper = mapper;
        this.validator = validator;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try{
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            validator.validateGetByIdRequest(request);

            Supplier Supplier = repository.findById(UUID.fromString(request.getParameter("supplierId")));
            SupplierResponseDto dto = mapper.mapEntityToEntityResponseDto(Supplier);

            setEntity(request, dto);

        } catch (DAOException | MappingException ex){

            Logger.error(this.getClass().getName(), ex.getMessage());
            page = PAGE_SUPPLIER_LIST;

            setException(request, ex);
        }
        return page;
    }
}
