package crud.dtos.retailers.responses;

import crud.model.enums.Roles;

public class RetailerResponseDto {
    public String name;
    public String email;
    public String imageLocation;
    public Roles roleName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public Roles getRoleName() {
        return roleName;
    }
    public void setRoleName(Roles roleName) {
        this.roleName = roleName;
    }
}
