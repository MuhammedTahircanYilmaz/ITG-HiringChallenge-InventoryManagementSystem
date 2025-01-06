package crud.service.bills.queries;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.base.ServiceResult;
import crud.dtos.bills.responses.BillResponseDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.BillMapper;
import crud.model.entities.Bill;
import crud.model.enums.Roles;
import crud.repository.BillRepository;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;

public class GetAllBillQuery extends AbstractCommand {

    private BillRepository repository;
    private String page = "";
    private AuthService authService;
    private final BillMapper mapper;

    public GetAllBillQuery(BillRepository repository, AuthService authService, BillMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.authService = authService;
    }

    @Override
    public ServiceResult execute(HttpServletRequest request) {

        try{
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);
            authService.hasRole(token, Roles.ADMIN.name());
            ArrayList<Bill> bills = repository.getAll();
            ArrayList<BillResponseDto> response = new ArrayList<BillResponseDto>();
            for (Bill bill : bills) {
                BillResponseDto dto = mapper.mapEntityToEntityResponseDto(bill);
                response.add(dto);
            }

            return createJsonResponse(response);

        } catch (DAOException | MappingException ex ){
            Logger.error(this.getClass().getName(),ex.getMessage());
            setException(request, ex);
        }
        return createView(page);
    }
}
