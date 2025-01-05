package crud.dtos.bills.requests;

import java.util.UUID;

public class GetByIdBillQueryDto {

    private UUID id;

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
