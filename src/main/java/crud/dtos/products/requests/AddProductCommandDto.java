package crud.dtos.products.requests;

import java.sql.Timestamp;
import java.util.UUID;

public class AddProductCommandDto {
    private UUID supplierId;
    private String name;
    private String description;
    private Long stockQuantity;
    private Double price;
    private Float discount;
    private Timestamp createdAt;
    private String createdBy;
    private String imageLocation;


    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public UUID getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(UUID supplierId) {
        if (supplierId == null) {
            throw new IllegalArgumentException("Supplier ID cannot be null.");
        }
        this.supplierId = supplierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty.");
        }
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Product description cannot be null or empty.");
        }
        this.description = description;
    }

    public Long getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Long stockQuantity) {
        if (stockQuantity == null || stockQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be null or negative.");
        }
        this.stockQuantity = stockQuantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        if (price == null || price <= 0) {
            throw new IllegalArgumentException("Price must be positive.");
        }
        this.price = price;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        if (discount != null && (discount < 0 || discount > 100)) {
            throw new IllegalArgumentException("Discount must be between 0 and 100.");
        }
        this.discount = discount;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        if (imageLocation == null || imageLocation.trim().isEmpty()) {
            throw new IllegalArgumentException("Image location cannot be null or empty.");
        }
        this.imageLocation = imageLocation;
    }
}
