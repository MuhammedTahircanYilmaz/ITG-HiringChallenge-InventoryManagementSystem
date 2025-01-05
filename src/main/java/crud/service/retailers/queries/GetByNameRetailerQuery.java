package crud.service.retailers.queries;

import crud.base.AbstractCommand;
import crud.exception.DAOException;
import crud.model.entities.Retailer;
import crud.repository.RetailerRepository;
import crud.service.validation.RetailerValidator;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;

public class GetByNameRetailerQuery extends AbstractCommand {

    private final RetailerRepository repository;
    private final RetailerValidator validator;
    private String page = PAGE_RETAILER_FORM;

    public GetByNameRetailerQuery(RetailerRepository repository, RetailerValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try {

            validator.validateGetByNameRequest(request);

            String retailerName = request.getParameter("name");

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