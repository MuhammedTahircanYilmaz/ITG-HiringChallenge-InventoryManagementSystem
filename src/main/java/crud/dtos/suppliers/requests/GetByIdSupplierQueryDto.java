package crud.dtos.suppliers.requests;

import java.util.UUID;

public class GetByIdSupplierQueryDto {
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