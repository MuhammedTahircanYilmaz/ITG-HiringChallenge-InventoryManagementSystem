package crud.controller;

import crud.exception.BusinessException;
import crud.authorization.login.LoginResponse;
import crud.authorization.login.LoginService;
import crud.util.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private final LoginService loginService;

    public LoginServlet(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String userType = request.getParameter("userType");

            if (email == null || password == null || userType == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Missing required parameters\"}");
                return;
            }

            LoginResponse loginResponse = loginService.login(email, password, userType);

            if (loginResponse.getToken() == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\":\"Invalid credentials\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(
                        String.format(
                                "{\"token\":\"%s\", \"userId\":\"%s\", \"email\":\"%s\", \"userType\":\"%s\"}",
                                loginResponse.getToken(),
                                loginResponse.getUserId(),
                                loginResponse.getEmail(),
                                loginResponse.getUserType()
                        )
                );
            }

        } catch (BusinessException ex) {
            Logger.warning(this.getClass().getName(), "Login failed: " + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (Exception ex) {
            Logger.error(this.getClass().getName(), "Internal error during login");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"An error occurred\"}");
        }
    }
}
