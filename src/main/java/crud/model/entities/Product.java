package crud.model.entities;

import crud.base.BaseEntity;

import java.util.UUID;

public class Product extends BaseEntity {

    private UUID supplierId;
    private String name;
    private String description;
    private Long stockQuantity;
    private Double price;
    private Float discount;
    private String imagePath;
    private boolean inStock;

    public UUID getSupplierId() {
        return supplierId;
        }

    public void setSupplierId(UUID supplierId ) {
        this.supplierId = supplierId;
        }

    public boolean isInStock() { return inStock; }

    public void setInStock(boolean inStock) { this.inStock = inStock; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
         }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Long getStockQuantity() {
        return stockQuantity;
        }
        
    public void setStockQuantity(Long stockQuantity) {
        this.stockQuantity = stockQuantity;
        }

    public Double getPrice() {
        return price;
        }

    public void setPrice(Double price) {
        this.price = price;
        }

    public Float getDiscount() {
        return discount;
        }

    public void setDiscount(Float discount) {
        this.discount = discount;
        }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "Product{" +
                "Id='" + this.getId() + '\'' +
                ", Name='" + name + '\'' +
                ", Stock Quantity=" + stockQuantity + '\'' +
                ", Price ='" + price + '\'' +
                ", Discount='" + discount +
                '}';
    }



}