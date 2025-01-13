package crud.service.bills.commands;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.base.ServiceResult;
import crud.dtos.bills.requests.UpdateBillCommandDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.BillMapper;
import crud.model.entities.Bill;
import crud.repository.bill.impl.BillRepositoryImpl;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class UpdateBillCommand  extends AbstractCommand {

    private String page = RETAILER_MAIN;
    private BillRepositoryImpl repository;
    private BillMapper mapper;
    private AuthService authService;

    public UpdateBillCommand(BillRepositoryImpl repository, BillMapper mapper , AuthService authService) {
        this.repository = repository;
        this.mapper = mapper;
        this.authService = authService;
    }

    @Override
    public ServiceResult execute(HttpServletRequest request) {
        try {
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            UUID userId = authService.getUserId(token);
            UUID billId = UUID.fromString(request.getParameter("billId"));

            Bill bill = repository.findById(billId);
            authService.isAllowed(token,bill.getRetailerId());

            UpdateBillCommandDto dto = mapper.mapUpdateRequestDto(request, userId.toString());

            bill.setAmount(dto.getAmount());
            bill.setCurrentPrice(dto.getCurrentPrice());
            bill.setUpdatedAt(dto.getUpdatedAt());
            bill.setUpdatedBy(dto.getUpdatedBy());

            repository.update(bill);
            addSuccessMessage(request, UPDATE_SUCCESS_MESSAGE);

        } catch (DAOException | MappingException ex) {

            Logger.error(this.getClass().getName(), ex.getMessage());
            page = RETAILER_MAIN;

            setException(request, ex);
        }
       return createView(page);
    }
}
