package crud.service.bills.queries;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.base.ServiceResult;
import crud.dtos.bills.responses.BillResponseDto;
import crud.exception.AuthenticationException;
import crud.mapper.BillMapper;
import crud.model.entities.Bill;
import crud.model.entities.Supplier;
import crud.repository.bill.impl.BillRepositoryImpl;
import crud.repository.supplier.SupplierRepositoryImpl;
import crud.service.validation.BillValidator;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.UUID;

public class GetBySupplierIdBillQuery extends AbstractCommand {

    private final BillRepositoryImpl repository;
    private final SupplierRepositoryImpl SupplierRepository;
    private final AuthService authService;
    private final BillMapper mapper;
    private String page = PAST_SALES;

    public GetBySupplierIdBillQuery(BillRepositoryImpl repository, BillValidator validator, AuthService authService, BillMapper mapper , SupplierRepositoryImpl SupplierRepository) {
        this.repository = repository;
        this.SupplierRepository = SupplierRepository;
        this.authService = authService;
        this.mapper = mapper;
    }

    @Override
    public ServiceResult execute(HttpServletRequest request) {
        try {

            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            Supplier Supplier = SupplierRepository.findById(UUID.fromString(request.getParameter("supplierId")));

            if(!Supplier.getId().equals(authService.getUserId(token)) ){
                throw new AuthenticationException("You are not allowed to view this bill");
            }

            mapper.mapGetBySupplierIdRequestDto(request);

            ArrayList<Bill> bills = repository.findBySupplierId(authService.getUserId(token));

            ArrayList<BillResponseDto> response = new ArrayList<BillResponseDto>();
            for (Bill bill : bills) {
                BillResponseDto dto = mapper.mapEntityToEntityResponseDto(bill);
                response.add(dto);
            }

            setEntities(request, response, "");
            return createJsonResponse(response);

        } catch (Exception ex) {

            Logger.error(this.getClass().getName(), ex.getMessage());

            page = PAST_SALES;

            setException(request, ex);
        }
        return createJsonResponse(page);
    }

}


