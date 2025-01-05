package crud.authorization.login;

import crud.exception.BusinessException;
import crud.model.entities.User;
import crud.repository.AdminRepository;
import crud.repository.RetailerRepository;
import crud.repository.SupplierRepository;
import crud.util.JwtUtil;
import crud.util.PasswordUtils;
import crud.util.Logger;

public class LoginService {
    private final RetailerRepository retailerRepository;
    private final SupplierRepository supplierRepository;
    private final AdminRepository adminRepository;
    private final JwtUtil jwtUtil;

    public LoginService(RetailerRepository retailerRepository, SupplierRepository supplierRepository,
                        AdminRepository adminRepository, JwtUtil jwtUtil) {
        this.retailerRepository = retailerRepository;
        this.supplierRepository = supplierRepository;
        this.adminRepository = adminRepository;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(String email, String password, String userRole) throws BusinessException {
        try {
            User user = null;

            if ("RETAILER".equalsIgnoreCase(userRole)) {
                user = retailerRepository.findByEmail(email);
            } else if ("SUPPLIER".equalsIgnoreCase(userRole)) {
                user = supplierRepository.findByEmail(email);
            } else if ("ADMIN".equalsIgnoreCase(userRole)) {
                user = adminRepository.findByEmail(email);
            } else {
                throw new BusinessException("Invalid user role");
            }

            if (user == null || !PasswordUtils.verifyPassword(password, user.getPassword())) {
                Logger.warning("LoginService", "Invalid credentials for email: " + email);
                throw new BusinessException("Invalid credentials");
            }

            String token = jwtUtil.generateToken(user.getId(), userRole);

            Logger.info("LoginService", "Successful login for email: " + email);
            return new LoginResponse(token, user.getId(), user.getEmail(), userRole);

        } catch (Exception e) {
            Logger.error("LoginService", e.getMessage());
            throw new BusinessException("An error occurred during login");
        }
    }
}