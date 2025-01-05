package crud.dtos.bills.requests;

import java.util.UUID;

public class DeleteBillCommandDto {
    private UUID id;
    private String deletedBy;
    private String deletedAt;

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Bill ID cannot be null.");
        }
        this.id = id;
    }
}
