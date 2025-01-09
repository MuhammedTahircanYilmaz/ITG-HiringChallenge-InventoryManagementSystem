package crud.infrastructure;

import crud.authorization.AuthService;
import crud.repository.AdminRepository;
import crud.repository.RetailerRepository;
import crud.repository.SupplierRepository;
import crud.repository.TokenRepository;

public class AuthorizationFactory {

    private final AuthService authService;

    public AuthorizationFactory(RetailerRepository retailerRepository, SupplierRepository supplierRepository, AdminRepository adminRepository, TokenRepository tokenRepository) {

        this.authService = new AuthService(adminRepository, retailerRepository, supplierRepository, new crud.util.JwtUtil(), tokenRepository);
    }

    public AuthService getAuthService() {
        return authService;
    }
}
