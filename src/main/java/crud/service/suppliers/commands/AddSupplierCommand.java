package crud.service.suppliers.commands;

import crud.base.AbstractCommand;
import crud.base.BaseMapper;
import crud.base.BaseRepository;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.model.Supplier;
import crud.repository.SupplierRepository;
import crud.util.PasswordUtils;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class AddSupplierCommand extends AbstractCommand {

    public AddSupplierCommand(SupplierRepository repository, BaseMapper<Supplier> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    private String page = PAGE_SUPPLIER_LIST;
    private SupplierRepository repository;
    private BaseMapper<Supplier> mapper;

    @Override
    public String execute(HttpServletRequest request) {
        try{

   //       SupplierValidator.validateAddRequest(request);

            Supplier supplier = mapper.buildEntity(request);

            String password = request.getParameter("password");
            String hashedPassword = PasswordUtils.hashPassword(password);
            supplier.setPassword(hashedPassword);

            repository.add(supplier);

            addSuccessMessage(request,ADD_SUCCESS_MESSAGE);

        } catch (DAOException| MappingException ex){
            //logger.error(e.getMessage());

            page = PAGE_SUPPLIER_FORM;
            setException(request, ex);
        }
        return page;
    }
}
