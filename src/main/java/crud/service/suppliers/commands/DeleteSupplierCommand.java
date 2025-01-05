package crud.service.suppliers.commands;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.dtos.suppliers.requests.DeleteSupplierCommandDto;
import crud.exception.AuthenticationException;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.SupplierMapper;
import crud.mapper.SupplierMapper;
import crud.model.entities.Supplier;
import crud.repository.SupplierRepository;
import crud.service.validation.SupplierValidator;
import crud.util.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class DeleteSupplierCommand extends AbstractCommand {

    private final SupplierRepository repository;
    private final SupplierValidator validator;
    private final AuthService authService;
    private final SupplierMapper mapper;
    private String page = PAGE_SUPPLIER_LIST;

    public DeleteSupplierCommand(SupplierRepository repository, SupplierValidator validator, AuthService authService, SupplierMapper mapper) {
        this.repository = repository;
        this.validator = validator;
        this.authService = authService;
        this.mapper = mapper;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try{
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            UUID userId = authService.getUserId(token);
            Supplier Supplier = repository.findById(userId);

            if(!authService.isAllowed(token,Supplier.getId())){
                throw new AuthenticationException("The user is not allowed to delete this Supplier");
            }

            DeleteSupplierCommandDto dto = mapper.mapDeleteRequestDto(request, userId.toString());

            Supplier.setDeletedBy(dto.getDeletedBy());
            Supplier.setDeletedAt(dto.getDeletedAt());
            validator.validateDeleteRequest(request);

            repository.delete(Supplier);
            addSuccessMessage(request,DELETE_SUCCESS_MESSAGE);

        } catch (DAOException | MappingException ex){
            Logger.error(this.getClass().getName(), ex.getMessage());
            page = PAGE_SUPPLIER_FORM;
            setException(request, ex);
        }
        return page;
    }
}
