package crud.service.bills.commands;

import crud.authorization.AuthService;
import crud.dtos.bills.requests.UpdateBillCommandDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.model.entities.Bill;
import crud.model.entities.Product;
import crud.model.enums.BillStatus;
import crud.repository.BillRepository;
import crud.repository.ProductRepository;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;


public class UpdateBillStatusCommand {
    private final BillRepository repository;
    private final ProductRepository productRepository;
    private final AuthService authService;
    private String page = "/bill/form.jsp";



    public UpdateBillStatusCommand(BillRepository billRepository, ProductRepository productRepository, AuthService authService) {
        this.repository = billRepository;
        this.authService = authService;
        this.productRepository = productRepository;
    }

    public String execute(HttpServletRequest request) {
        try {
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            UUID userId = authService.getUserId(token);
            UUID billId = UUID.fromString(request.getParameter("billId"));

            Bill bill = repository.findById(billId);
            authService.isAllowed(token,bill.getRetailerId());

            bill.setStatus(BillStatus.valueOf( request.getParameter("status")));

            repository.update(bill);
            if(bill.getStatus().equals(BillStatus.PAID)){
                updateProductStock(bill);
            }

            Logger.info(this.getClass().getName(), "Status update Successful");

        } catch (Exception ex) {

            Logger.error(this.getClass().getName(), ex.getMessage());
        }
        return page;
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
            productRepository.update(product);

            Logger.info(this.getClass().getName(),
                    String.format("Updated stock for product %d: %d -> %d",
                            product.getId(), product.getStockQuantity() + bill.getAmount(), newStock));
        }catch (Exception ex){
            Logger.error(this.getClass().getName(), ex.getMessage());
        }
    }
}