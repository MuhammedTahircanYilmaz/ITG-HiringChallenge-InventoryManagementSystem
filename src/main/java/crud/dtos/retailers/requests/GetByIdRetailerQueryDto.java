package crud.dtos.retailers.requests;

import java.util.UUID;

public class GetByIdRetailerQueryDto {
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null.");
        }
        this.id = id;
    }
}