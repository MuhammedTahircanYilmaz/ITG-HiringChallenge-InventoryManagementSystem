package crud.service.retailers.commands;

import crud.base.AbstractCommand;
import crud.base.BaseMapper;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.model.Retailer;
import crud.repository.RetailerRepository;
import crud.util.PasswordUtils;
import jakarta.servlet.http.HttpServletRequest;

public class AddRetailerCommand extends AbstractCommand {

    public AddRetailerCommand(RetailerRepository repository, BaseMapper<Retailer> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    private String page = PAGE_RETAILER_LIST;
    private RetailerRepository repository;
    private BaseMapper<Retailer> mapper;

    @Override
    public String execute(HttpServletRequest request) {
        try{

   //       RetailerValidator.validateAddRequest(request);

            Retailer retailer = mapper.buildEntity(request);

            String password = request.getParameter("password");
            String hashedPassword = PasswordUtils.hashPassword(password);
            retailer.setPassword(hashedPassword);

            repository.add(retailer);

            addSuccessMessage(request,ADD_SUCCESS_MESSAGE);

        } catch (DAOException| MappingException ex){
            //logger.error(e.getMessage());

            page = PAGE_RETAILER_FORM;
            setException(request, ex);
        }
        return page;
    }
}
