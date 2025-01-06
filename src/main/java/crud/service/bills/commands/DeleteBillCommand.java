package crud.service.bills.commands;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.base.ServiceResult;
import crud.dtos.bills.requests.DeleteBillCommandDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.BillMapper;
import crud.model.entities.Bill;
import crud.repository.BillRepository;
import crud.service.validation.BillValidator;
import crud.util.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class DeleteBillCommand extends AbstractCommand {

    private final BillRepository repository;
    private final BillValidator validator;
    private final BillMapper mapper;
    private final AuthService authService;
    private String page = RETAILER_MAIN;


    public DeleteBillCommand(BillRepository repository, BillValidator validator, BillMapper mapper, AuthService authSer) {
        this.repository = repository;
        this.authService = authSer;
        this.mapper = mapper;
        this.validator = validator;
    }

    @Override
    public ServiceResult execute(HttpServletRequest request) {
        try{
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            UUID userId = authService.getUserId(token);
            UUID billId = UUID.fromString(request.getParameter("billId"));
            Bill bill = repository.findById(billId);

            authService.isAllowed(token,bill.getRetailerId());

            DeleteBillCommandDto dto = mapper.mapDeleteRequestDto(request, userId.toString());
            bill.setDeletedBy(dto.getDeletedBy());
            bill.setDeletedAt(dto.getDeletedAt());
            repository.delete(bill);

            addSuccessMessage(request,DELETE_SUCCESS_MESSAGE);
            Logger.info(this.getClass().getName(), DELETE_SUCCESS_MESSAGE);

        } catch (DAOException | MappingException ex){
            Logger.error(this.getClass().getName(), ex.getMessage());
            page = RETAILER_MAIN;
            setException(request, ex);
        }
        return createView(page);
    }
}
