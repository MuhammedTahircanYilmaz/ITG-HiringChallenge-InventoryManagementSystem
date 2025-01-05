package crud.dtos.bills.requests;

import java.util.UUID;

public class GetBySupplierIdBillQueryDto {

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
