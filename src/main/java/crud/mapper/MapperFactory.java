package crud.mapper;

public class MapperFactory {

    public static ProductMapper getProductMapper() { return new ProductMapper();}
    public static SupplierMapper getSupplierMapper() { return new SupplierMapper();}
    public static RetailerMapper getRetailerMapper() { return new RetailerMapper();}
    public static BillMapper getBillMapper() { return new BillMapper();}
}
