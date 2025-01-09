package crud.service.suppliers.queries;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.base.ServiceResult;
import crud.dtos.suppliers.responses.SupplierResponseDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.SupplierMapper;
import crud.model.entities.Supplier;
import crud.model.entities.Supplier;
import crud.repository.SupplierRepository;
import crud.service.validation.SupplierValidator;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;

public class GetByNameSupplierQuery extends AbstractCommand {

    private final SupplierRepository repository;
    private final AuthService authService ;
    private final SupplierMapper mapper;
    private String page = "";

    public GetByNameSupplierQuery(SupplierRepository repository, AuthService authService, SupplierMapper mapper) {
        this.repository = repository;
        this.authService = authService;
        this.mapper = mapper;
    }

    @Override
    public ServiceResult execute(HttpServletRequest request) {
        try {

            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            ArrayList<Supplier> Suppliers = repository.findAllByName(request.getParameter("supplierName"));
            ArrayList<SupplierResponseDto> response = new ArrayList<>();

            for(Supplier Supplier : Suppliers){
                SupplierResponseDto dto = mapper.mapEntityToEntityResponseDto(Supplier);
                response.add(dto);
            }

            return createJsonResponse(response);

        } catch (DAOException | IllegalArgumentException | MappingException ex) {

            Logger.error(this.getClass().getName(), ex.getMessage());

            page = "";

            setException(request, ex);
        }
        return createView(page);
    }
}