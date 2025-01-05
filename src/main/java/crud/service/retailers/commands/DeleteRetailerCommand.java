package crud.service.retailers.commands;

import crud.base.AbstractCommand;
import crud.exception.DAOException;
import crud.repository.RetailerRepository;
import crud.service.validation.RetailerValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

public class DeleteRetailerCommand extends AbstractCommand {

    private final RetailerRepository repository;
    private final RetailerValidator validator;
    private String page = PAGE_RETAILER_LIST;

    public DeleteRetailerCommand(RetailerRepository repository , RetailerValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try{
            validator.validateDeleteRequest(request);

            repository.delete(getId(request));
            addSuccessMessage(request,DELETE_SUCCESS_MESSAGE);

        } catch (DAOException | ServletException ex){
            //logger.error(ex.getMessage);
            page = PAGE_RETAILER_FORM;
            setException(request, ex);
        }
        return page;
    }
}
