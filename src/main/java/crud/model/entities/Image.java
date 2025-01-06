package crud.model.entities;

import crud.base.BaseEntity;

public class Image extends BaseEntity {

    private String imageLocation;
    private String productId;

    public String getImageLocation() {
        return imageLocation;
    }
    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }
    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }
}
