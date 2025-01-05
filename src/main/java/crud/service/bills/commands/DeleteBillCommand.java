package crud.service.bills.commands;

import crud.base.AbstractCommand;
import crud.exception.DAOException;
import crud.repository.BillRepository;
import crud.service.validation.BillValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

public class DeleteBillCommand extends AbstractCommand {

    private final BillRepository repository;
    private final BillValidator validator;
    private String page = PAGE_BILL_LIST;


    public DeleteBillCommand(BillRepository repository, BillValidator validator) {
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
            page = PAGE_BILL_FORM;
            setException(request, ex);
        }
        return page;
    }
}
