package crud.controller;

import crud.exception.BusinessException;
import crud.authorization.login.LoginResponse;
import crud.authorization.login.LoginCommand;
import crud.util.Logger;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class LoginServlet extends HttpServlet {

    public LoginServlet(){}

    private LoginCommand loginCommand;
    public LoginServlet(LoginCommand loginCommand) {
        this.loginCommand = loginCommand;
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String roleName = request.getParameter("roleName");

            if (email == null || password == null || roleName == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Missing required parameters\"}");
                return;
            }

            LoginResponse loginResponse =(LoginResponse) loginCommand.execute(request).getData();

            if (loginResponse.getToken() == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\":\"Invalid credentials\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(
                        String.format(
                                "{\"token\":\"%s\", \"userId\":\"%s\", \"email\":\"%s\", \"roleName\":\"%s\"}",
                                loginResponse.getToken(),
                                loginResponse.getUserId(),
                                loginResponse.getEmail(),
                                loginResponse.getRoleName()
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
