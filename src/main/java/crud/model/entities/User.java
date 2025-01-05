package crud.model.entities;


import crud.base.BaseEntity;
import crud.model.enums.Roles;

public class User extends BaseEntity {

    private String name;
    private String email;
    private String password;
    private String imageLocation;
    private Roles roleName;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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




    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", id=" + this.getId() + '\'' +
                ", roleName='" + roleName +
                '}';
    }
}