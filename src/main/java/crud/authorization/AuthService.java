package crud.authorization;

import crud.exception.DAOException;
import crud.model.entities.User;
import crud.repository.admin.impl.AdminRepositoryImpl;
import crud.repository.retailer.RetailerRepositoryImpl;
import crud.repository.supplier.SupplierRepositoryImpl;
import crud.repository.TokenRepositoryImpl;
import crud.util.JwtUtil;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class AuthService {
    private AdminRepositoryImpl adminRepositoryImpl;
    private RetailerRepositoryImpl retailerRepository;
    private SupplierRepositoryImpl supplierRepository;
    private TokenRepositoryImpl tokenRepository;
    private JwtUtil jwtUtil;

    public AuthService(AdminRepositoryImpl adminRepositoryImpl,
                       RetailerRepositoryImpl retailerRepository, SupplierRepositoryImpl supplierRepository, JwtUtil jwtUtil, TokenRepositoryImpl tokenRepository) {
       this.adminRepositoryImpl = adminRepositoryImpl;
       this.retailerRepository = retailerRepository;
       this.supplierRepository = supplierRepository;
       this.tokenRepository = tokenRepository;

       this.jwtUtil = jwtUtil;
    }

    public boolean isAuthenticated(String token) {
        try{
            UUID userId = UUID.fromString(jwtUtil.getUserId(token));
            if (userId == null) {
                return false;
            }
            tokenRepository.findByUserId(userId);
        }catch ( Exception ex) {
            Logger.log("AdminRepository", ex.getMessage(), Logger.Level.ERROR);
        }
        return true;
    }


    public boolean hasRole(String token, String requiredRole) {
        try {
            UUID userId = UUID.fromString(jwtUtil.getUserId(token));
            String userRole = jwtUtil.getRoleName(token);

            return requiredRole.equalsIgnoreCase(userRole) && userExistsWithRole(userId, requiredRole) != null;
        } catch (Exception e) {
            Logger.error("AuthService", e.getMessage());
            return false;
        }
    }

    public boolean isAllowed(String token, UUID userId) {
        try {
            String userRole = jwtUtil.getRoleName(token);
            return "ADMIN".equalsIgnoreCase(userRole) || jwtUtil.getUserId(token).equals(userId.toString());
        } catch (Exception e) {
            Logger.error("AuthService", e.getMessage());
        }
        return false;
    }

    public UUID getUserId(String token) {
        return UUID.fromString(jwtUtil.getUserId(token));
    }

    private User userExistsWithRole(UUID userId, String role) {

        try{
            if ("RETAILER".equalsIgnoreCase(role)) {
                return retailerRepository.findById(userId);
            } else if ("SUPPLIER".equalsIgnoreCase(role)) {
                return supplierRepository.findById(userId);
            } else if ("ADMIN".equalsIgnoreCase(role)) {
                return adminRepositoryImpl.findById(userId);
            }
            return null;
        } catch (DAOException ex){
            Logger.error("AuthService", ex.getMessage());
        }
        return null;
    }

    public String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new SecurityException("Missing or invalid Authorization header.");
        }
        return authorizationHeader.substring(7);
    }
}