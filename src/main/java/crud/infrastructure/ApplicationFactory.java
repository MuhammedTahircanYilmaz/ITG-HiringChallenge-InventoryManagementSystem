package crud.infrastructure;

import crud.exception.DAOException;

public class ApplicationFactory {
    public final AuthorizationFactory authorizationFactory;
    public final BusinessRulesFactory businessRulesFactory;
    public final MapperFactory mapperFactory;
    public final RepositoryFactory repositoryFactory;
    public final ServiceFactory serviceFactory;
    public final ValidatorFactory validatorFactory;


    public ApplicationFactory() throws DAOException {
        this.repositoryFactory = new RepositoryFactory();
        this.authorizationFactory = new AuthorizationFactory(
                repositoryFactory.getRetailerRepository(),
                repositoryFactory.getSupplierRepository(),
                repositoryFactory.getAdminRepository(),
                repositoryFactory.getTokenRepository()
        );
        this.businessRulesFactory = new BusinessRulesFactory(
                repositoryFactory.getRetailerRepository(),
                repositoryFactory.getProductRepository(),
                repositoryFactory.getSupplierRepository(),
                repositoryFactory.getBillRepository()
        );
        this.mapperFactory = new MapperFactory(
                repositoryFactory.getProductRepository(),
                repositoryFactory.getSupplierRepository()
        );
        this.validatorFactory = new ValidatorFactory(
                businessRulesFactory.getBillBusinessRules(),
                businessRulesFactory.getProductBusinessRules(),
                businessRulesFactory.getSupplierBusinessRules(),
                businessRulesFactory.getRetailerBusinessRules()
        );
        this.serviceFactory = new ServiceFactory(
                validatorFactory,
                repositoryFactory,
                authorizationFactory,
                mapperFactory
        );
    }


    public AuthorizationFactory getAuthorizationFactory() {
        return authorizationFactory;
    }
    public BusinessRulesFactory getBusinessRulesFactory() {
        return businessRulesFactory;
    }
    public MapperFactory getMapperFactory() {
        return mapperFactory;
    }
    public RepositoryFactory getRepositoryFactory() {
        return repositoryFactory;
    }
    public ServiceFactory getServiceFactory() {
        return serviceFactory;
    }
    public ValidatorFactory getValidatorFactory() {
        return validatorFactory;
    }
}
