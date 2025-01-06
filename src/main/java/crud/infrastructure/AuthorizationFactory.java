package crud.infrastructure;

import crud.authorization.AuthService;
import crud.repository.AdminRepository;
import crud.repository.RetailerRepository;
import crud.repository.SupplierRepository;
import crud.repository.TokenRepository;

public class AuthorizationFactory {

    private final AuthService authService;
    private final RetailerRepository retailerRepository;
    private final SupplierRepository supplierRepository;
    private final TokenRepository tokenRepository;
    private final AdminRepository adminRepository;

    public AuthorizationFactory(RetailerRepository retailerRepository, SupplierRepository supplierRepository, AdminRepository adminRepository, TokenRepository tokenRepository) {
        this.retailerRepository = retailerRepository;
        this.supplierRepository = supplierRepository;
        this.adminRepository = adminRepository;
        this.tokenRepository = tokenRepository;
        this.authService = new AuthService(adminRepository, retailerRepository, supplierRepository, new crud.util.JwtUtil(), tokenRepository);
    }

    public AuthService getAuthService() {
        return authService;
    }
}
