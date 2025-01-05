package crud.dtos.suppliers.requests;
import java.sql.Timestamp;
import java.util.UUID;

public class UpdateSupplierCommandDto {
    private UUID id;
    private String name;
    private String imageLocation;
    private String updatedBy;
    private Timestamp updatedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null.");
        }
        this.id = id;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && name.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be empty if provided.");
        }
        this.name = name;
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