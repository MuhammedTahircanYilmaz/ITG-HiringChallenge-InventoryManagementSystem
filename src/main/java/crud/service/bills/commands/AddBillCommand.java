package crud.service.bills.commands;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.base.ServiceResult;
import crud.dtos.bills.requests.AddBillCommandDto;
import crud.exception.AuthenticationException;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.BillMapper;
import crud.model.entities.Bill;
import crud.model.entities.Product;
import crud.model.enums.Roles;
import crud.repository.AdminRepository;
import crud.repository.BillRepository;
import crud.repository.ProductRepository;
import crud.service.validation.BillValidator;
import crud.util.JwtUtil;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;


public class AddBillCommand extends AbstractCommand {

    private final BillRepository repository;
    private final BillMapper mapper;
    private final ProductRepository productRepository;
    private final AuthService authService;
    private String page = CURRENT_PURCHASES;

    public AddBillCommand(BillRepository repository, ProductRepository productRepository, BillMapper mapper, AuthService authService) {
        this.repository = repository;
        this.mapper = mapper;
        this.productRepository = productRepository;
        this.authService = authService;
    }

    @Override
    public ServiceResult execute(HttpServletRequest request) {
        try {
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            if(!authService.hasRole(token, Roles.RETAILER.toString()) ){
                throw new AuthenticationException("The User is not allowed to add a Bill");
            }

            AddBillCommandDto dto = mapper.mapAddRequestDto(request, authService.getUserId(token).toString());
            Bill bill = mapper.mapAddEntityDtoToEntity(dto);
            updateProductStock(bill);
            repository.add(bill);

            addSuccessMessage(request, ADD_SUCCESS_MESSAGE);

        } catch(MappingException| DAOException |SQLException ex)
        {
            Logger.error(this.getClass().getName(),ex.getMessage() );
            page = RETAILER_MAIN;
            setException(request, ex);
        }
        return createView(page);
    }
    private void updateProductStock( Bill bill) throws SQLException {
        try{
            Product product = productRepository.findById(bill.getProductId());
            if (product == null) {
                throw new SQLException("Product not found for bill: " + bill.getId());
            }

            long newStock = product.getStockQuantity() - bill.getAmount();

            if (newStock < 0) {
                throw new SQLException("Insufficient stock for product: " + product.getId());
            }

            product.setStockQuantity(newStock);

            if (newStock == 0) {
                product.setInStock(false);
            }

            productRepository.update(product);

            Logger.info(this.getClass().getName(),
                    String.format("Updated stock for product %d: %d -> %d",
                            product.getId(), product.getStockQuantity() + bill.getAmount(), newStock));

        }catch (Exception ex){
            Logger.error(this.getClass().getName(), ex.getMessage());
        }
    }
}

