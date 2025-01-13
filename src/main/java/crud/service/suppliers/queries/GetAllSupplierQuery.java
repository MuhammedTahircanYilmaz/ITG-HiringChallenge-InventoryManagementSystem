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
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;


public class GetAllSupplierQuery extends AbstractCommand {
    public GetAllSupplierQuery(SupplierRepositoryImpl repository, AuthService authService, SupplierMapper mapper) {
        this.repository = repository;
        this.authService = authService;
        this.mapper = mapper;
    }

    private SupplierRepositoryImpl repository;
    private final AuthService authService;
    private final SupplierMapper mapper;

    private String page = "";

    @Override
    public ServiceResult execute(HttpServletRequest request) {
            try{
                String token = authService.extractToken(request);
                authService.isAuthenticated(token);

                ArrayList<Supplier> Suppliers = repository.findAll();
                ArrayList<SupplierResponseDto> response = new ArrayList<>();

                for(Supplier Supplier : Suppliers){
                    SupplierResponseDto dto = mapper.mapEntityToEntityResponseDto(Supplier);
                    response.add(dto);
                }

                return createJsonResponse(response);
        } catch (DAOException | MappingException ex){
            Logger.error(this.getClass().getName(), ex.getMessage());
            setException(request, ex);

        }
        return createView(page);
    }
}
