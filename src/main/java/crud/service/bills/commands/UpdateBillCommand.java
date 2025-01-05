package crud.service.bills.commands;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.base.BaseMapper;
import crud.dtos.bills.requests.AddBillCommandDto;
import crud.dtos.bills.requests.UpdateBillCommandDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.BillMapper;
import crud.model.entities.Bill;
import crud.model.entities.Product;
import crud.repository.BillRepository;
import crud.repository.ProductRepository;
import crud.service.validation.BillValidator;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class UpdateBillCommand  extends AbstractCommand {

    private String page = PAGE_BILL_FORM;
    private BillRepository repository;
    private ProductRepository productRepository;
    private BillValidator validator;
    private BillMapper mapper;
    private AuthService authService;

    public UpdateBillCommand(BillRepository repository, BillMapper mapper , ProductRepository productRepository , BillValidator validator, AuthService authService) {
        this.repository = repository;
        this.validator = validator;
        this.mapper = mapper;
        this.productRepository = productRepository;
        this.authService = authService;
    }

    @Override
    public String execute(HttpServletRequest request) {
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
            page = PAGE_BILL_FORM;

            setException(request, ex);
        }
        return page;
    }
}
