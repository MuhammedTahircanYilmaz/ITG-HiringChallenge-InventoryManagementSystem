package crud.model.entities;

import crud.base.BaseEntity;
import crud.model.enums.BillStatus;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

public class Bill extends BaseEntity {

    private UUID supplierId;
    private UUID retailerId;
    private UUID productId;
    private Long amount;
    private Double currentPrice;
    private BillStatus status;
    private Timestamp date;

    public UUID getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(UUID supplierId) {
        this.supplierId = supplierId;
    }

    public UUID getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(UUID retailerId) {
        this.retailerId = retailerId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public BillStatus getStatus() {
        return status;
    }
    public void setStatus(BillStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "Id='" + this.getId() + '\'' +
                ", Supplier Id='" + supplierId + '\'' +
                ", Stock Quantity=" + retailerId + '\'' +
                ", Product Id ='" + productId + '\'' +
                ", Amount ='" + amount + '\'' +
                ", Current Price='" + currentPrice +'\'' +
                ", Date='" + date +
                '}';
    }
}