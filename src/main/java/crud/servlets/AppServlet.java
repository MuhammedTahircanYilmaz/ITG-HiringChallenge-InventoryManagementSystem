package crud.servlets;

import crud.base.AbstractCommand;
import crud.base.BaseController;
import crud.exception.DAOException;
import crud.infrastructure.ApplicationFactory;
import crud.infrastructure.ServiceFactory;
import crud.util.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AppServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final Map<String, BaseController> controllers = new HashMap<>();

    public AppServlet() throws DAOException {
    }

    @Override
    public void init() throws ServletException {
        try {
            controllers.put("bills", new BillController());
            controllers.put("products", new ProductController());
            controllers.put("products/images", new ImageController());
            controllers.put("retailers", new RetailerController());
            controllers.put("suppliers", new SupplierController());
            controllers.put("login", new LoginController());
        } catch (DAOException e) {
            throw new ServletException("Error initializing controllers", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();

        if (path == null || path.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request path");
            return;
        }

        String[] parts = path.split("/");
        if (parts.length < 2) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request path");
            return;
        }

        String controllerKey = parts[1];
        BaseController controller = controllers.get(controllerKey);

        if (controller == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Controller not found for path: " + path);
            return;
        }

        controller.handle(request, response);

    }
}