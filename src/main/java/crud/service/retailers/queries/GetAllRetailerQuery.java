package crud.service.retailers.queries;

import crud.base.AbstractCommand;
import crud.exception.DAOException;
import crud.model.Retailer;
import crud.repository.RetailerRepository;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;


public class GetAllRetailerQuery extends AbstractCommand {
    public GetAllRetailerQuery(RetailerRepository repository) {
        this.repository = repository;
    }

    private RetailerRepository repository;
    private String page = PAGE_RETAILER_LIST;

    @Override
    public String execute(HttpServletRequest request) {
        try{

            ArrayList<Retailer> retailers = repository.getAll();

            setEntities(request, retailers,"");

        } catch (DAOException ex){
            // logger.error(ex.getMessage());
            setException(request, ex);

        }
        return page;
    }
}
