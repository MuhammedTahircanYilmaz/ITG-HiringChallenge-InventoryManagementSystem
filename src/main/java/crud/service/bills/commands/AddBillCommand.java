package crud.service.bills.commands;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.dtos.bills.requests.AddBillCommandDto;
import crud.exception.AuthenticationException;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.BillMapper;
import crud.model.entities.Bill;
import crud.repository.AdminRepository;
import crud.repository.BillRepository;
import crud.repository.ProductRepository;
import crud.service.validation.BillValidator;
import crud.util.JwtUtil;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;


public class AddBillCommand extends AbstractCommand {

    private final BillRepository repository;
    private final BillMapper mapper;
    private final ProductRepository productRepository;
    private final BillValidator validator;
    private final AuthService authService;
    private String page = PAGE_BILL_FORM;

    public AddBillCommand(BillRepository repository, ProductRepository productRepository, BillMapper mapper, BillValidator validator, AuthService authService) {
        this.repository = repository;
        this.mapper = mapper;
        this.productRepository = productRepository;
        this.validator = validator;
        this.authService = authService;

    }

    @Override
    public String execute(HttpServletRequest request) {
        try {
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            if(!authService.hasRole(token, "Retailer") ){
                throw new AuthenticationException("The User is not allowed to add a Bill");
            }

            AddBillCommandDto dto = mapper.mapAddRequestDto(request, authService.getUserId(token).toString());
            Bill bill = mapper.mapAddEntityDtoToEntity(dto);
            repository.add(bill);

            addSuccessMessage(request, ADD_SUCCESS_MESSAGE);
        } catch(MappingException| DAOException ex)
        {
            Logger.error(this.getClass().getName(),ex.getMessage() );
            page = PAGE_BILL_FORM;
            setException(request, ex);
        }
        return page;

    }
}

