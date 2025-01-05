package crud.service.retailers.queries;

import crud.base.AbstractCommand;
import crud.exception.DAOException;
import crud.model.entities.Retailer;
import crud.repository.RetailerRepository;
import crud.service.validation.RetailerValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

public class GetByIdRetailerQuery extends AbstractCommand {

    private final RetailerRepository repository;
    private final RetailerValidator validator;
    private String page = PAGE_RETAILER_LIST;

    public GetByIdRetailerQuery(RetailerRepository repository , RetailerValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try{
            validator.validateGetByIdRequest(request);
            Retailer retailer = repository.findById(getId(request));

            setEntity(request, retailer);
        } catch (DAOException | ServletException ex){

            //logger.error(ex.getMessage());
            page = PAGE_RETAILER_LIST;

            setException(request, ex);
        }
        return page;
    }
}
