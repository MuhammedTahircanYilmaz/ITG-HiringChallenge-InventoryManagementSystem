package crud.service.bills.commands;

import crud.model.entities.Bill;
import crud.model.entities.Product;
import crud.model.enums.BillStatus;
import crud.repository.BillRepository;
import crud.repository.ProductRepository;
import crud.util.Logger;
import java.sql.Connection;
import java.sql.SQLException;

public class UpdateBillStatusCommand {
    private final BillRepository billRepository;
    private final ProductRepository productRepository;
    private static final Logger logger = new Logger();

    public UpdateBillStatusCommand(BillRepository billRepository, ProductRepository productRepository) {
        this.billRepository = billRepository;
        this.productRepository = productRepository;
    }

    public void execute(Long billId, BillStatus newStatus, String updatedBy) throws SQLException {
        Connection conn = null;
        try {
            conn = billRepository.getConnectionFactory().getConnection();
            conn.setAutoCommit(false);

            Bill bill = billRepository.findById(billId);
            if (bill == null) {
                throw new SQLException("Bill not found with id: " + billId);
            }

            if (newStatus == BillStatus.ACCEPTED && bill.getStatus() != BillStatus.ACCEPTED) {
                // Update product stock
                updateProductStock(conn, bill);
            }

            // Update bill status
            bill.setStatus(newStatus);
            bill.setUpdatedBy(updatedBy);
            billRepository.update(bill);

            conn.commit();
            Logger.info(this.getClass().getName(),
                    String.format("Bill %d status updated to %s", billId, newStatus));
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            Logger.error(this.getClass().getName(),
                    String.format("Error updating bill %d status: %s", billId, e.getMessage()));
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    private void updateProductStock(Connection conn, Bill bill) throws SQLException {
        Product product = productRepository.findById(bill.getProductId());
        if (product == null) {
            throw new SQLException("Product not found for bill: " + bill.getId());
        }

        int newStock = product.getStock() - bill.getQuantity();
        if (newStock < 0) {
            throw new SQLException("Insufficient stock for product: " + product.getId());
        }

        product.setStock(newStock);
        productRepository.update(product);

        Logger.info(this.getClass().getName(),
                String.format("Updated stock for product %d: %d -> %d",
                        product.getId(), product.getStock() + bill.getQuantity(), newStock));
    }
}