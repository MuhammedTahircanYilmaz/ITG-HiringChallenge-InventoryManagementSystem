package crud.service.bills.queries;

import crud.base.AbstractCommand;
import crud.exception.DAOException;
import crud.model.entities.Bill;
import crud.repository.BillRepository;
import crud.service.validation.BillValidator;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.UUID;

public class GetByRetailerIdBillQuery extends AbstractCommand {

    private final BillRepository repository;
    private final BillValidator validator;
    private String page = PAGE_BILL_FORM;

    public GetByRetailerIdBillQuery(BillRepository repository, BillValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try {
            UUID RetailerId = UUID.fromString(request.getParameter("RetailerId"));
            validator.validateGetByRetailerIdRequest(request);

            ArrayList<Bill> bills = repository.findByRetailerId(RetailerId);

            setEntities(request, bills, "");

        } catch (DAOException | IllegalArgumentException ex) {

            // logger.error(ex.getMessage());

            page = PAGE_BILL_LIST;

            setException(request, ex);
        }
        return page;
    }

}

