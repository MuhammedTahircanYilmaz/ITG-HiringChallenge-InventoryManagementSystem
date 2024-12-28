public class Product {

    private Guid id;
    private Long supplierId;
    private String name;
    private Long stockQuantity;
    private Double price;
    private Float discount;

    public getId(){
        return id;
        }

    public setId(Guid id ) {
        this.id = id;
        }
    
    public getSupplierId() {
        return supplierId;
        }

    public setSupplierId(long supplierId ) { 
        this.supplierId = supplierId;
        }

    public getName() {
        return name;
    }

    public setName(String name) {
        this.name = name;
         }

    public getStockQuantity() {
        return stockQuantity;
        }
        
    public setStockQuantity(Long stockQuantity) {
        this.stockQuantity = stockQuantity;
        }

    public getPrice() {
        return price;
        }

    public setPrice(Double price) {
        this.price = price;
        }

    public getDiscount() {
        return discount;
        }

    public setDiscount(Float discount) {
        this.discount = discount;
        }


    @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}