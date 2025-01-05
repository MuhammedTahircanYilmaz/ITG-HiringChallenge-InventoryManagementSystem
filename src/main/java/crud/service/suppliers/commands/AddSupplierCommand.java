package crud.service.suppliers.commands;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.dtos.suppliers.requests.AddSupplierCommandDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.SupplierMapper;
import crud.model.entities.Supplier;
import crud.repository.SupplierRepository;
import crud.service.validation.SupplierValidator;
import crud.util.Logger;
import crud.util.PasswordUtils;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class AddSupplierCommand extends AbstractCommand {

    public AddSupplierCommand(SupplierRepository repository, SupplierMapper mapper, SupplierValidator validator, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
        this.validator = validator;
        this.mapper = mapper;
    }

    private String page = PAGE_SUPPLIER_LIST;
    private SupplierRepository repository;
    private SupplierValidator validator;
    private AuthService authService;
    private SupplierMapper mapper;

    @Override
    public String execute(HttpServletRequest request) {
        try{

            validator.validateAddRequest(request);
            UUID userId = UUID.randomUUID();
            AddSupplierCommandDto dto = mapper.mapAddRequestDto(request, userId.toString());
            Supplier supplier = mapper.mapAddEntityDtoToEntity(dto);

            String hashedPassword = PasswordUtils.hashPassword(supplier.getPassword());
            supplier.setPassword(hashedPassword);

            repository.add(supplier);

            addSuccessMessage(request,ADD_SUCCESS_MESSAGE);

        } catch (DAOException| MappingException ex){
            Logger.error(this.getClass().getName(), ex.getMessage());

            page = PAGE_SUPPLIER_FORM;
            setException(request, ex);
        }
        return page;
    }
}
