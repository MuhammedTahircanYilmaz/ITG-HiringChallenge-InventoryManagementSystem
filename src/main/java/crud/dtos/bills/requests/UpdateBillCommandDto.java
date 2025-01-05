package crud.dtos.bills.requests;

import java.sql.Timestamp;
import java.util.UUID;

public class UpdateBillCommandDto {
    private UUID id;
    private UUID productId;
    private Long amount;
    private Double currentPrice;
    private String updatedBy;
    private Timestamp updatedAt;

    public UUID getId() {
        return id;
    }

    public UUID getProductId() {
        return productId;
    }

    public Long getAmount() {
        return amount;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp date) {
        this.updatedAt =  date;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setProductId(UUID productId) {
        if (productId != null) {
            this.productId = productId;
        }
    }

    public void setAmount(Long amount) {
        if (amount != null && amount > 0) {
            this.amount = amount;
        }
    }

    public void setCurrentPrice(Double currentPrice) {
        if (currentPrice != null && currentPrice > 0) {
            this.currentPrice = currentPrice;
        }
    }

}