package crud.authorization;

import crud.exception.DAOException;
import crud.model.entities.User;
import crud.repository.AdminRepository;
import crud.repository.RetailerRepository;
import crud.repository.SupplierRepository;
import crud.util.JwtUtil;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class AuthService {
    private AdminRepository adminRepository;
    private RetailerRepository retailerRepository;
    private SupplierRepository supplierRepository;
    private JwtUtil jwtUtil;

    public AuthService(AdminRepository adminRepository,
                       RetailerRepository retailerRepository, SupplierRepository supplierRepository, JwtUtil jwtUtil) {
       this.adminRepository = adminRepository;
       this.retailerRepository = retailerRepository;
       this.supplierRepository = supplierRepository;
       this.jwtUtil = jwtUtil;
    }

    public boolean isAuthenticated(String token) {
        try{
            UUID userId = UUID.fromString(jwtUtil.getUserId(token));
            if (userId == null) {
                return false;
            }
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

    private User userExistsWithRole(UUID userId, String role) {

        try{
            if ("RETAILER".equalsIgnoreCase(role)) {
                return retailerRepository.findById(userId);
            } else if ("SUPPLIER".equalsIgnoreCase(role)) {
                return supplierRepository.findById(userId);
            } else if ("ADMIN".equalsIgnoreCase(role)) {
                return adminRepository.findById(userId);
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