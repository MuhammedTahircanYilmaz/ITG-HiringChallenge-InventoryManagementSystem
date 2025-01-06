package crud.service.bills.queries;


import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.base.ServiceResult;
import crud.dtos.bills.responses.BillResponseDto;
import crud.exception.AuthenticationException;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.BillMapper;
import crud.model.entities.Bill;
import crud.repository.BillRepository;
import crud.service.validation.BillValidator;
import crud.util.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

public class GetByIdBillQuery extends AbstractCommand {

    private final BillRepository repository;
    private final BillValidator validator;
    private final AuthService authService;
    private final BillMapper mapper;
    private String page = "";

    public GetByIdBillQuery(BillRepository repository, BillValidator validator, AuthService authService , BillMapper mapper) {
        this.repository = repository;
        this.validator = validator;
        this.authService = authService;
        this.mapper = mapper;
    }

    @Override
    public ServiceResult execute(HttpServletRequest request) {
        try{
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            validator.validateGetByIdRequest(request);
            Bill bill = repository.findById(getId(request));


            if(!authService.isAllowed(token,bill.getRetailerId()) || !authService.isAllowed(token,bill.getSupplierId())){
                throw new AuthenticationException("You are not allowed to view this bill");
            }
            BillResponseDto dto = mapper.mapEntityToEntityResponseDto(bill);
            return createJsonResponse(dto);

        } catch (DAOException | ServletException | MappingException ex){

            Logger.error(this.getClass().getName(),ex.getMessage());
                page = "";

            setException(request, ex);
        }
        return createView(page);
    }
}
