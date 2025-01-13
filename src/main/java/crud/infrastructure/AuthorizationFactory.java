package crud.infrastructure;

import crud.authorization.AuthService;
import crud.repository.admin.impl.AdminRepositoryImpl;
import crud.repository.retailer.RetailerRepositoryImpl;
import crud.repository.supplier.SupplierRepositoryImpl;
import crud.repository.TokenRepositoryImpl;

public class AuthorizationFactory {

    private final AuthService authService;

    public AuthorizationFactory(RetailerRepositoryImpl retailerRepository, SupplierRepositoryImpl supplierRepository, AdminRepositoryImpl adminRepositoryImpl, TokenRepositoryImpl tokenRepository) {

        this.authService = new AuthService(adminRepositoryImpl, retailerRepository, supplierRepository, new crud.util.JwtUtil(), tokenRepository);
    }

    public AuthService getAuthService() {
        return authService;
    }
}
