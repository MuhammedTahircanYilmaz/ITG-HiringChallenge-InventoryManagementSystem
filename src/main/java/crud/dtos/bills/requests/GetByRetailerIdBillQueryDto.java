package crud.dtos.bills.requests;

import java.util.UUID;

public class GetByRetailerIdBillQueryDto {

    private UUID retailerId;

    public UUID getId() {
        return retailerId;
    }

    public void setId(UUID retailerId) {
        if (retailerId == null) {
            throw new IllegalArgumentException("Bill retailerId cannot be null.");
        }
        this.retailerId = retailerId;
    }
}
