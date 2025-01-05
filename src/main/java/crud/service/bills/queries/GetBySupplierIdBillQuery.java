package crud.service.bills.queries;

import crud.base.AbstractCommand;
import crud.exception.DAOException;
import crud.model.entities.Bill;
import crud.repository.BillRepository;
import crud.service.validation.BillValidator;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.UUID;

public class GetBySupplierIdBillQuery extends AbstractCommand {

    private final BillRepository repository;
    private final BillValidator validator;
    private String page = PAGE_BILL_FORM;

    public GetBySupplierIdBillQuery(BillRepository repository, BillValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try {
            UUID supplierId = UUID.fromString(request.getParameter("supplierId"));
            validator.validateGetBySupplierIdRequest(request);

            ArrayList<Bill> bills = repository.findBySupplierId(supplierId);

            setEntities(request, bills, "");

        } catch (DAOException | IllegalArgumentException ex) {

            // logger.error(ex.getMessage());

            page = PAGE_BILL_LIST;

            setException(request, ex);
        }
        return page;
    }

}

