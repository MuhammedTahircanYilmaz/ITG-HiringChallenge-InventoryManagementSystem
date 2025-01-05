package crud.dtos.products.requests;

import java.sql.Timestamp;
import java.util.UUID;

public class UpdateProductCommandDto  {
    private UUID id;
    private String name;
    private String description;
    private Long stockQuantity;
    private Double price;
    private Float discount;
    private String imageLocation;
    private String updatedBy;
    private Timestamp updatedAt;


    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Product ID cannot be null.");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty if provided.");
        }
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description != null && description.trim().isEmpty()) {
            throw new IllegalArgumentException("Product description cannot be empty if provided.");
        }
        this.description = description;
    }

    public Long getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Long stockQuantity) {
        if (stockQuantity != null && stockQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative.");
        }
        this.stockQuantity = stockQuantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        if (price != null && price <= 0) {
            throw new IllegalArgumentException("Price must be positive if provided.");
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
        if (imageLocation != null && imageLocation.trim().isEmpty()) {
            throw new IllegalArgumentException("Image location cannot be empty if provided.");
        }
        this.imageLocation = imageLocation;
    }
}