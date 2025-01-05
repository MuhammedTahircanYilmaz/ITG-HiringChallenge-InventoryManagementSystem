package crud.dtos.bills.requests;

import java.util.UUID;

public class GetByProductIdBillQueryDto {

    private UUID productId;

    public UUID getId() {
        return productId;
    }

    public void setId(UUID productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Bill productId cannot be null.");
        }
        this.productId = productId;
    }
}
