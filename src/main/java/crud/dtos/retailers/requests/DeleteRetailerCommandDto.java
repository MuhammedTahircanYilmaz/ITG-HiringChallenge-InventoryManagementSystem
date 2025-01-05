package crud.dtos.retailers.requests;

import java.sql.Timestamp;
import java.util.UUID;

public class DeleteRetailerCommandDto {
    private UUID id;
    private String deletedBy;
    private Timestamp deletedAt;

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Timestamp getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Timestamp deletedAt) {
        this.deletedAt = deletedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Retailer ID cannot be null.");
        }
        this.id = id;
    }
}
