package crud.model;

import crud.base.BaseEntity;

import java.sql.Date;
import java.util.UUID;

public class Bill extends BaseEntity {

    private UUID supplierId;
    private UUID retailerId;
    private UUID productId;
    private Long amount;
    private Double currentPrice;
    private Date date;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Product{" +
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