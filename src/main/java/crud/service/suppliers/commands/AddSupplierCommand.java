package crud.service.suppliers.commands;

import crud.base.AbstractCommand;
import crud.base.ServiceResult;
import crud.dtos.suppliers.requests.AddSupplierCommandDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.SupplierMapper;
import crud.model.entities.Supplier;
import crud.repository.supplier.SupplierRepositoryImpl;
import crud.util.Logger;
import crud.util.PasswordUtils;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class AddSupplierCommand extends AbstractCommand {

    public AddSupplierCommand(SupplierRepositoryImpl repository, SupplierMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    private String page = REGISTER;
    private SupplierRepositoryImpl repository;
    private SupplierMapper mapper;

    @Override
    public ServiceResult execute(HttpServletRequest request) {
        try{

            UUID userId = UUID.randomUUID();
            AddSupplierCommandDto dto = mapper.mapAddRequestDto(request, userId.toString());
            Supplier supplier = mapper.mapAddEntityDtoToEntity(dto);

            String hashedPassword = PasswordUtils.hashPassword(supplier.getPassword());
            supplier.setPassword(hashedPassword);

            repository.add(supplier);

            addSuccessMessage(request,ADD_SUCCESS_MESSAGE);

        } catch (DAOException| MappingException ex){
            Logger.error(this.getClass().getName(), ex.getMessage());

            page = REGISTER;
            setException(request, ex);
        }
        return createView(page);
    }
}
