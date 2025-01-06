package crud.service.suppliers.commands;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.dtos.suppliers.requests.UpdateSupplierCommandDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.SupplierMapper;
import crud.model.entities.Supplier;
import crud.repository.SupplierRepository;
import crud.service.validation.SupplierValidator;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class UpdateSupplierCommand extends AbstractCommand {

    public UpdateSupplierCommand(SupplierRepository repository, SupplierMapper mapper, SupplierValidator validator, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
        this.validator = validator;
        this.mapper = mapper;
    }

    private String page = PAGE_SUPPLIER_LIST;
    private SupplierValidator validator;
    private SupplierRepository repository;
    private SupplierMapper mapper;
    private final AuthService authService;

    @Override
    public String execute(HttpServletRequest request) {
        try{

            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            UUID userId = authService.getUserId(token);
            UUID SupplierId = UUID.fromString(request.getParameter("supplierId"));

            mapper.mapUpdateRequestDto(request, userId.toString());

            Supplier Supplier = repository.findById(SupplierId);

            authService.isAllowed(token,Supplier.getId());

            UpdateSupplierCommandDto dto = mapper.mapUpdateRequestDto(request, userId.toString());

            Supplier.setImageLocation(dto.getImageLocation());
            Supplier.setName(dto.getName());
            Supplier.setUpdatedAt(dto.getUpdatedAt());
            Supplier.setUpdatedBy(dto.getUpdatedBy());

            repository.update(Supplier);

            addSuccessMessage(request,UPDATE_SUCCESS_MESSAGE);

        } catch(DAOException | MappingException ex){

            Logger.error(this.getClass().getName(), ex.getMessage());
            page = PAGE_SUPPLIER_FORM;

            setException(request, ex);
        }
        return page;
    }
}
