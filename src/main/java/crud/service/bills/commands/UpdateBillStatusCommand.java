package crud.service.bills.commands;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.base.ServiceResult;
import crud.model.entities.Bill;
import crud.model.enums.BillStatus;
import crud.repository.bill.impl.BillRepositoryImpl;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;


public class UpdateBillStatusCommand extends AbstractCommand {
    private final BillRepositoryImpl repository;
    private final AuthService authService;
    private String page = PENDING_BILLS;



    public UpdateBillStatusCommand(BillRepositoryImpl billRepositoryImpl, AuthService authService) {
        this.repository = billRepositoryImpl;
        this.authService = authService;
    }

    public ServiceResult execute(HttpServletRequest request) {
        try {
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            UUID billId = UUID.fromString(request.getParameter("billId"));

            Bill bill = repository.findById(billId);
            authService.isAllowed(token,bill.getSupplierId());

            bill.setStatus(BillStatus.valueOf( request.getParameter("status")));

            if (bill.getStatus().toString().equals("REJECTED")){
                repository.delete(bill);
                return createView(page);
            }
                repository.update(bill);

            Logger.info(this.getClass().getName(), "Status update Successful");

        } catch (Exception ex) {

            Logger.error(this.getClass().getName(), ex.getMessage());
        }
        return createView(page);
    }


}