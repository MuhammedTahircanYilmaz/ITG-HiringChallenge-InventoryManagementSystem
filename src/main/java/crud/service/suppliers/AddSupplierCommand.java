package crud.service.suppliers;

import crud.base.AbstractCommand;
import crud.base.BaseMapper;
import crud.base.BaseRepository;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.model.Supplier;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;
import java.util.UUID;

public class AddSupplierCommand extends AbstractCommand {

    private String page = PAGE_SUPPLIER_LIST;
    private BaseRepository<Supplier, UUID> repository;
    private BaseMapper<Supplier> mapper;

    @Override
    public String execute(HttpServletRequest request) {
        try{

            SupplierValidator.validate(request);

            Supplier supplier = mapper.buildEntity(request);

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
