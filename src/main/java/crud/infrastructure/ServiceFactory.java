package crud.infrastructure;


import crud.service.bills.commands.*;
import crud.service.bills.queries.*;
import crud.service.products.commands.*;
import crud.service.products.queries.*;
import crud.service.retailers.commands.*;
import crud.service.retailers.queries.*;
import crud.service.suppliers.commands.*;
import crud.service.suppliers.queries.*;

public class ServiceFactory {
    private final ValidatorFactory validatorFactory;
    private final RepositoryFactory repositoryFactory;
    private final AuthorizationFactory authorizationFactory;
    private final MapperFactory mapperFactory;

    public ServiceFactory(ValidatorFactory validatorFactory, RepositoryFactory repositoryFactory, AuthorizationFactory authorizationFactory, MapperFactory mapperFactory ) {
        this.validatorFactory = validatorFactory;
        this.repositoryFactory = repositoryFactory;
        this.authorizationFactory = authorizationFactory;
        this.mapperFactory = mapperFactory;
    }

    // Bill Commands
    public AddBillCommand createAddBillCommand() {
        return new AddBillCommand(
                repositoryFactory.getBillRepository(),
                repositoryFactory.getProductRepository(),
                mapperFactory.getBillMapper(),
                validatorFactory.getBillValidator(),
                authorizationFactory.getAuthService()
        );
    }

    public UpdateBillCommand createUpdateBillCommand() {
        return new UpdateBillCommand(
                repositoryFactory.getBillRepository(),
                mapperFactory.getBillMapper(),
                repositoryFactory.getProductRepository(),
                validatorFactory.getBillValidator(),
                authorizationFactory.getAuthService()
        );
    }

    public DeleteBillCommand createDeleteBillCommand() {
        return new DeleteBillCommand(
                repositoryFactory.getBillRepository(),
                validatorFactory.getBillValidator(),
                mapperFactory.getBillMapper(),
                authorizationFactory.getAuthService()
        );
    }

    public UpdateBillStatusCommand createUpdateBillStatusCommand() {
        return new UpdateBillStatusCommand(
                repositoryFactory.getBillRepository(),
                repositoryFactory.getProductRepository(),
                authorizationFactory.getAuthService()
        );
    }

    // Product Commands
    public AddProductCommand createAddProductCommand() {
        return new AddProductCommand(
                repositoryFactory.getProductRepository(),
                mapperFactory.getProductMapper(),
                validatorFactory.getProductValidator(),
                authorizationFactory.getAuthService()
        );
    }

    public UpdateProductCommand createUpdateProductCommand() {
        return new UpdateProductCommand(
                repositoryFactory.getProductRepository(),
                mapperFactory.getProductMapper(),
                validatorFactory.getProductValidator(),
                authorizationFactory.getAuthService()
        );
    }

    public DeleteProductCommand createDeleteProductCommand() {
        return new DeleteProductCommand(
                repositoryFactory.getProductRepository(),
                validatorFactory.getProductValidator(),
                mapperFactory.getProductMapper(),
                authorizationFactory.getAuthService()
        );
    }

    // Retailer Commands
    public AddRetailerCommand createAddRetailerCommand() {
        return new AddRetailerCommand(
                repositoryFactory.getRetailerRepository(),
                mapperFactory.getRetailerMapper(),
                validatorFactory.getRetailerValidator()
        );
    }

    public UpdateRetailerCommand createUpdateRetailerCommand() {
        return new UpdateRetailerCommand(
                repositoryFactory.getRetailerRepository(),
                mapperFactory.getRetailerMapper(),
                validatorFactory.getRetailerValidator(),
                authorizationFactory.getAuthService()
        );
    }

    public DeleteRetailerCommand createDeleteRetailerCommand() {
        return new DeleteRetailerCommand(
                repositoryFactory.getRetailerRepository(),
                validatorFactory.getRetailerValidator(),
                authorizationFactory.getAuthService(),
                mapperFactory.getRetailerMapper()
        );
    }

    // Supplier Commands
    public AddSupplierCommand createAddSupplierCommand() {
        return new AddSupplierCommand(
                repositoryFactory.getSupplierRepository(),
                mapperFactory.getSupplierMapper(),
                validatorFactory.getSupplierValidator(),
                authorizationFactory.getAuthService()
        );
    }

    public UpdateSupplierCommand createUpdateSupplierCommand() {
        return new UpdateSupplierCommand(
                repositoryFactory.getSupplierRepository(),
                mapperFactory.getSupplierMapper(),
                validatorFactory.getSupplierValidator(),
                authorizationFactory.getAuthService()
        );
    }

    public DeleteSupplierCommand createDeleteSupplierCommand() {
        return new DeleteSupplierCommand(
                repositoryFactory.getSupplierRepository(),
                validatorFactory.getSupplierValidator(),
                authorizationFactory.getAuthService(),
                mapperFactory.getSupplierMapper()
        );
    }

    // Bill Queries
    public GetAllBillQuery createGetAllBillQuery() {
        return new GetAllBillQuery(
                repositoryFactory.getBillRepository(),
                authorizationFactory.getAuthService(),
                mapperFactory.getBillMapper()
        );
    }

    public GetByIdBillQuery createGetBillByIdQuery() {
        return new GetByIdBillQuery(
                repositoryFactory.getBillRepository(),
                validatorFactory.getBillValidator(),
                authorizationFactory.getAuthService(),
                mapperFactory.getBillMapper()
        );

    }

    public GetByProductIdBillQuery createGetBillsByProductQuery() {
        return new GetByProductIdBillQuery(
                repositoryFactory.getBillRepository(),
                validatorFactory.getBillValidator(),
                authorizationFactory.getAuthService(),
                mapperFactory.getBillMapper(),
                repositoryFactory.getProductRepository()
        );
    }

    public GetByRetailerIdBillQuery createGetBillsByRetailerQuery() {
        return new GetByRetailerIdBillQuery(
                repositoryFactory.getBillRepository(),
                validatorFactory.getBillValidator(),
                authorizationFactory.getAuthService(),
                mapperFactory.getBillMapper(),
                repositoryFactory.getRetailerRepository()
        );
    }

    public GetBySupplierIdBillQuery createGetBillsBySupplierQuery() {
        return new GetBySupplierIdBillQuery(
                repositoryFactory.getBillRepository(),
                validatorFactory.getBillValidator(),
                authorizationFactory.getAuthService(),
                mapperFactory.getBillMapper(),
                repositoryFactory.getSupplierRepository()
        );
    }

    // Product Queries
    public GetAllProductQuery createGetAllProductQuery() {
        return new GetAllProductQuery(
                repositoryFactory.getProductRepository(),
                authorizationFactory.getAuthService(),
                mapperFactory.getProductMapper()
        );
    }

    public GetByIdProductQuery createGetProductByIdQuery() {
        return new GetByIdProductQuery(
                repositoryFactory.getProductRepository(),
                validatorFactory.getProductValidator(),
                authorizationFactory.getAuthService(),
                mapperFactory.getProductMapper()

        );
    }

    public GetBySupplierIdProductQuery createGetProductsBySupplierQuery() {
        return new GetBySupplierIdProductQuery(
                repositoryFactory.getProductRepository(),
                validatorFactory.getProductValidator(),
                authorizationFactory.getAuthService(),
                mapperFactory.getProductMapper(),
                repositoryFactory.getSupplierRepository()
        );
    }

    // Retailer Queries
    public GetAllRetailerQuery createGetAllRetailerQuery() {
        return new GetAllRetailerQuery(
                repositoryFactory.getRetailerRepository(),
                authorizationFactory.getAuthService(),
                mapperFactory.getRetailerMapper()
        );
    }

    public GetByIdRetailerQuery createGetRetailerByIdQuery() {
        return new GetByIdRetailerQuery(
                repositoryFactory.getRetailerRepository(),
                validatorFactory.getRetailerValidator(),
                authorizationFactory.getAuthService(),
                mapperFactory.getRetailerMapper()
        );
    }

    public GetByNameRetailerQuery createGetRetailerByNameQuery() {
        return new GetByNameRetailerQuery(
                repositoryFactory.getRetailerRepository(),
                validatorFactory.getRetailerValidator(),
                authorizationFactory.getAuthService(),
                mapperFactory.getRetailerMapper()
        );
    }

    // Supplier Queries
    public GetAllSupplierQuery createGetAllSupplierQuery() {
        return new GetAllSupplierQuery(
                repositoryFactory.getSupplierRepository(),
                authorizationFactory.getAuthService(),
                mapperFactory.getSupplierMapper()
        );
    }

    public GetByIdSupplierQuery createGetSupplierByIdQuery() {
        return new GetByIdSupplierQuery(
                repositoryFactory.getSupplierRepository(),
                validatorFactory.getSupplierValidator(),
                authorizationFactory.getAuthService(),
                mapperFactory.getSupplierMapper()
        );
    }

    public GetByNameSupplierQuery createGetSupplierByNameQuery() {
        return new GetByNameSupplierQuery(
                repositoryFactory.getSupplierRepository(),
                validatorFactory.getSupplierValidator(),
                authorizationFactory.getAuthService(),
                mapperFactory.getSupplierMapper()
        );
    }


}
