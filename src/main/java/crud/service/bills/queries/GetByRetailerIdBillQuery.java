package crud.service.bills.queries;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.dtos.bills.responses.BillResponseDto;
import crud.exception.AuthenticationException;
import crud.exception.DAOException;
import crud.mapper.BillMapper;
import crud.model.entities.Bill;
import crud.model.entities.Retailer;
import crud.model.enums.Roles;
import crud.repository.BillRepository;
import crud.repository.RetailerRepository;
import crud.service.validation.BillValidator;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.UUID;

public class GetByRetailerIdBillQuery extends AbstractCommand {

    private final BillRepository repository;
    private final RetailerRepository retailerRepository;
    private final BillValidator validator;
    private final AuthService authService;
    private final BillMapper mapper;
    private String page = PAGE_BILL_FORM;

    public GetByRetailerIdBillQuery(BillRepository repository, BillValidator validator, AuthService authService, BillMapper mapper , RetailerRepository retailerRepository) {
        this.repository = repository;
        this.validator = validator;
        this.retailerRepository = retailerRepository;
        this.authService = authService;
        this.mapper = mapper;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try {

            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            Retailer retailer = retailerRepository.findById(UUID.fromString(request.getParameter("retailerId")));

            if(!retailer.getId().equals(authService.getUserId(token)) ){
                throw new AuthenticationException("You are not allowed to view this bill");
            }

            mapper.mapGetByRetailerIdRequestDto(request);

            ArrayList<Bill> bills = repository.findByRetailerId(authService.getUserId(token));

            ArrayList<BillResponseDto> response = new ArrayList<BillResponseDto>();
            for (Bill bill : bills) {
                BillResponseDto dto = mapper.mapEntityToEntityResponseDto(bill);
                response.add(dto);
            }

            setEntities(request, response, "");

        } catch (Exception ex) {

            Logger.error(this.getClass().getName(), ex.getMessage());

            page = PAGE_BILL_LIST;

            setException(request, ex);
        }
        return page;
    }

}

