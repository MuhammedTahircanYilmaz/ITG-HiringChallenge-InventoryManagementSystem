package crud.dtos.suppliers.requests;

public class GetByEmailSupplierQueryDto {

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            throw new IllegalArgumentException("Email is invalid.");
        }
        this.email = email;
    }
}
