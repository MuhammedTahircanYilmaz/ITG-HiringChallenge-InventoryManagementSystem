package crud.dtos.products.requests;

import java.util.UUID;

public class GetByIdProductQueryDto {
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Product ID cannot be null.");
        }
        this.id = id;
    }
}