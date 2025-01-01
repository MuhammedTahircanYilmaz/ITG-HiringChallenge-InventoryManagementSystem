package crud.service.retailers.queries;

import crud.base.AbstractCommand;
import crud.exception.DAOException;
import crud.model.Retailer;
import crud.repository.RetailerRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

public class GetByIdRetailerQuery extends AbstractCommand {

    private final RetailerRepository repository;
    private String page = PAGE_RETAILER_LIST;

    public GetByIdRetailerQuery(RetailerRepository repository) {
        this.repository = repository;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try{
            Retailer retailer = repository.findById(getId(request));

            setEntity(request, retailer);
        } catch (DAOException | ServletException ex){

            //logget.error(ex.getMessage());
            page = PAGE_RETAILER_LIST;

            setException(request, ex);
        }
        return page;
    }
}
