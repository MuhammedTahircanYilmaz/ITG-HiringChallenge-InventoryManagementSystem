package crud.service.bills.queries;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.base.ServiceResult;
import crud.dtos.bills.responses.BillResponseDto;
import crud.exception.AuthenticationException;
import crud.exception.DAOException;
import crud.mapper.BillMapper;
import crud.model.entities.Bill;
import crud.model.entities.Product;
import crud.model.enums.Roles;
import crud.repository.BillRepository;
import crud.repository.ProductRepository;
import crud.service.validation.BillValidator;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.UUID;

public class GetByProductIdBillQuery extends AbstractCommand {

    private final BillRepository repository;
    private final ProductRepository productRepository;
    private final BillValidator validator;
    private final AuthService authService;
    private String page = "";
    private final BillMapper mapper;

    public GetByProductIdBillQuery(BillRepository repository, BillValidator validator, AuthService authService, BillMapper mapper, ProductRepository productRepository) {
        this.repository = repository;
        this.validator = validator;
        this.authService = authService;
        this.mapper = mapper;
        this.productRepository = productRepository;

    }

    @Override
    public ServiceResult execute(HttpServletRequest request) {
        try {

            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            validator.validateGetByIdRequest(request);

            UUID ProductId = UUID.fromString(request.getParameter("productId"));
            Product product = productRepository.findById(ProductId);

            if(product.getSupplierId().equals(authService.getUserId(token)) ){
                throw new AuthenticationException("You are not allowed to view this bill");
            }

            mapper.mapGetByProductIdRequestDto(request);

            ArrayList<Bill> bills = repository.findByProductId(product.getId());

            ArrayList<BillResponseDto> response = new ArrayList<BillResponseDto>();
            for (Bill bill : bills) {
                BillResponseDto dto = mapper.mapEntityToEntityResponseDto(bill);
                response.add(dto);
            }

           return createJsonResponse(response);


        } catch (Exception ex) {

            Logger.error(this.getClass().getName(), ex.getMessage());

            page = "";

            setException(request, ex);
        }
        return createView(page);
    }

}

