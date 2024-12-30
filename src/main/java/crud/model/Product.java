package crud.model;

import crud.base.BaseEntity;

import java.util.UUID;

public class Product extends BaseEntity {

    private Long supplierId;
    private String name;
    private String description;
    private Long stockQuantity;
    private Double price;
    private Float discount;
    private String imageLocation;

    public Long getSupplierId() {
        return supplierId;
        }

    public void setSupplierId(long supplierId ) {
        this.supplierId = supplierId;
        }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
         }

    public String getDescription() { return description; }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
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