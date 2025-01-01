package crud.service.retailers.queries;

import crud.base.AbstractCommand;
import crud.exception.DAOException;
import crud.model.Retailer;
import crud.repository.RetailerRepository;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;

public class GetByNameRetailerQuery extends AbstractCommand {

    private final RetailerRepository repository;
    private String page = PAGE_RETAILER_FORM;

    public GetByNameRetailerQuery(RetailerRepository repository) {
        this.repository = repository;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try {
            String retailerName = request.getParameter("name");
            if (retailerName == null || retailerName.trim().isEmpty()) {

                throw new IllegalArgumentException("retailer name cannot be null or empty");
            }

            ArrayList<Retailer> retailer = repository.findAllByName(retailerName);

            setEntity(request, retailer);

        } catch (DAOException  | IllegalArgumentException ex) {

            // logger.error(ex.getMessage());

            page = PAGE_RETAILER_LIST;

            setException(request, ex);
        }
        return page;
    }
}