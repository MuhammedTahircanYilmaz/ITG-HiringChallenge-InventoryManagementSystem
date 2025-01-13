package crud.service.bills.queries;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.base.ServiceResult;
import crud.dtos.bills.responses.BillResponseDto;
import crud.exception.AuthenticationException;
import crud.mapper.BillMapper;
import crud.model.entities.Bill;
import crud.model.entities.Retailer;
import crud.repository.bill.impl.BillRepositoryImpl;
import crud.repository.retailer.RetailerRepositoryImpl;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.UUID;

public class GetByRetailerIdBillQuery extends AbstractCommand {

    private final BillRepositoryImpl repository;
    private final RetailerRepositoryImpl retailerRepository;
    private final AuthService authService;
    private final BillMapper mapper;
    private String page = PAST_PURCHASES;

    public GetByRetailerIdBillQuery(BillRepositoryImpl repository, AuthService authService, BillMapper mapper , RetailerRepositoryImpl retailerRepository) {
        this.repository = repository;
        this.retailerRepository = retailerRepository;
        this.authService = authService;
        this.mapper = mapper;
    }

    @Override
    public ServiceResult execute(HttpServletRequest request) {
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

            return createJsonResponse(response);


        } catch (Exception ex) {

            Logger.error(this.getClass().getName(), ex.getMessage());

            page = PAST_PURCHASES;

            setException(request, ex);
        }
        return createView(page);
    }

}

