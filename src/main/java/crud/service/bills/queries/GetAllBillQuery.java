package crud.service.bills.queries;

import crud.base.AbstractCommand;
import crud.exception.DAOException;
import crud.model.entities.Bill;
import crud.repository.BillRepository;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;

public class GetAllBillQuery extends AbstractCommand {

    private BillRepository repository;
    private String page = PAGE_BILL_LIST;

    public GetAllBillQuery(BillRepository repository) {
        this.repository = repository;
    }

    @Override
    public String execute(HttpServletRequest request) {

        try{
            ArrayList<Bill> bills = repository.getAll();

            setEntities(request, bills,"");
        } catch (DAOException ex){

            //logger.error(ex.getMessage());
            setException(request, ex);
        }
        return page;
    }
}
