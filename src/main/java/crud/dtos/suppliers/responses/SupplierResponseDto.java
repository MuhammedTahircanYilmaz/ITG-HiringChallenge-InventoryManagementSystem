package crud.dtos.suppliers.responses;

import crud.model.enums.Roles;

public class SupplierResponseDto {
    public String name;
    public String email;
    public String imageLocation;
    public Roles roleName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException("Name cannot exceed 50 characters.");
        }
        this.name = name.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }
        if (!email.matches("^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        this.email = email.trim();
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        if (imageLocation == null || imageLocation.trim().isEmpty()) {
            throw new IllegalArgumentException("Image location cannot be null or empty.");
        }
        if (!imageLocation.matches("^(http|https):\\/\\/.*$")) {
            throw new IllegalArgumentException("Image location must be a valid URL.");
        }
        this.imageLocation = imageLocation.trim();
    }

    public Roles getRoleName() {
        return roleName;
    }
    public void setRoleName(Roles roleName) {
        this.roleName = roleName;
    }
}
