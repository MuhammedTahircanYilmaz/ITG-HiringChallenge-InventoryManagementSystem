public class Retailer implements BaseEntity {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String photoLocation;


    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

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

    public String getPhotoLocation() {
        return photoLocation;
     }

    public void setPhotoLocation(String photoLocation) { 
        this.photoLocation = photoLocation;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}