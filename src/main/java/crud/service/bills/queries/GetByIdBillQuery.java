package crud.service.bills.queries;


import crud.base.AbstractCommand;
import crud.exception.DAOException;
import crud.model.entities.Bill;
import crud.repository.BillRepository;
import crud.service.validation.BillValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

public class GetByIdBillQuery extends AbstractCommand {

    private final BillRepository repository;
    private final BillValidator validator;
    private String page = PAGE_BILL_FORM;

    public GetByIdBillQuery(BillRepository repository, BillValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try{
            validator.validateGetByIdRequest(request);
            Bill bill = repository.findById(getId(request));

            setEntity(request, bill);
        } catch (DAOException | ServletException ex){

            //logget.error(ex.getMessage());
            page = PAGE_BILL_LIST;

            setException(request, ex);
        }
        return page;
    }
}
