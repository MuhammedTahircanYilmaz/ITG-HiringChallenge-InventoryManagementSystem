package crud.dtos.bills.requests;

import crud.model.enums.BillStatus;

import java.sql.Timestamp;
import java.util.UUID;

public class AddBillCommandDto {
    private UUID supplierId;
    private UUID retailerId;
    private UUID productId;
    private String createdBy;
    private Long amount;
    private Double currentPrice;
    private Timestamp createdAt;
    private Timestamp date;
    private BillStatus status;


    public UUID getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(UUID supplierId) {
        if (supplierId == null) {
            throw new IllegalArgumentException("Supplier ID cannot be null.");
        }
        this.supplierId = supplierId;
    }

    public UUID getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(UUID retailerId) {
        if (retailerId == null) {
            throw new IllegalArgumentException("Retailer ID cannot be null.");
        }
        this.retailerId = retailerId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null.");
        }
        this.productId = productId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        this.amount = amount;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        if (currentPrice == null || currentPrice <= 0) {
            throw new IllegalArgumentException("Current price must be positive.");
        }
        this.currentPrice = currentPrice;
    }

    public Timestamp getDate() {
        return date;
    }


    public void setDate(Timestamp date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }
        this.date = date;
    }
    public Timestamp getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Timestamp date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }
        this.createdAt = this.date;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        if (createdBy == null) {
            throw new IllegalArgumentException("Created by cannot be null.");
        }
        this.createdBy = createdBy;
    }

    public BillStatus getStatus() {
        return status;
    }
    public void setStatus(BillStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }
        this.status = status;
    }
}