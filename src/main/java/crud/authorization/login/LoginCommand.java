package crud.authorization.login;

import crud.base.AbstractCommand;
import crud.base.ServiceResult;
import crud.exception.BusinessException;
import crud.model.entities.User;
import crud.repository.AdminRepository;
import crud.repository.RetailerRepository;
import crud.repository.SupplierRepository;
import crud.util.JwtUtil;
import crud.util.PasswordUtils;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginCommand extends AbstractCommand {
    private final RetailerRepository retailerRepository;
    private final SupplierRepository supplierRepository;
    private final AdminRepository adminRepository;
    private final JwtUtil jwtUtil;

    public LoginCommand(RetailerRepository retailerRepository, SupplierRepository supplierRepository,
                        AdminRepository adminRepository, JwtUtil jwtUtil) {
        this.retailerRepository = retailerRepository;
        this.supplierRepository = supplierRepository;
        this.adminRepository = adminRepository;
        this.jwtUtil = jwtUtil;
    }


    @Override
    public ServiceResult execute(HttpServletRequest request) throws Exception {
        try {
            User user = null;
            String roleName = request.getParameter("roleName");
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            if (email == null || password == null || roleName == null) {
                throw new BusinessException("There are parameters missing");
            }

            if ("RETAILER".equalsIgnoreCase(roleName)) {
                user = retailerRepository.findByEmail(email);
            } else if ("SUPPLIER".equalsIgnoreCase(roleName)) {
                user = supplierRepository.findByEmail(email);
            } else if ("ADMIN".equalsIgnoreCase(roleName)) {
                user = adminRepository.findByEmail(email);
            } else {
                throw new BusinessException("Invalid user role");
            }

            if (user == null || !PasswordUtils.verifyPassword(password, user.getPassword())) {
                Logger.warning("LoginService", "Invalid credentials for email: " + email);
                throw new BusinessException("Invalid credentials");
            }

            String token = jwtUtil.generateToken(user.getId(), roleName);

            Logger.info("LoginService", "Successful login for email: " + email);
            LoginResponse response = new LoginResponse(token, user.getId(), user.getEmail(), roleName);
            setEntity(request, response);
            return createView(LOGIN);

        } catch (Exception e) {
            Logger.error("LoginService", e.getMessage());
            throw new BusinessException("An error occurred during login");
        }
    }
}