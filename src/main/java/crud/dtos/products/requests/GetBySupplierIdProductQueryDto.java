package crud.dtos.products.requests;

import java.util.UUID;

public class GetBySupplierIdProductQueryDto {

    private UUID supplierId;

    public UUID getId() {
        return supplierId;
    }

    public void setId(UUID supplierId) {
        if (supplierId == null) {
            throw new IllegalArgumentException("Bill supplierId cannot be null.");
        }
        this.supplierId = supplierId;
    }
}
