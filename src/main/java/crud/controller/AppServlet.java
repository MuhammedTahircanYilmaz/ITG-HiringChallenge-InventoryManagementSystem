package crud.controller;

import crud.base.AbstractCommand;
import crud.exception.DAOException;
import crud.infrastructure.ApplicationFactory;
import crud.infrastructure.ServiceFactory;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/app")
public class AppServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final Map<String, AbstractCommand> commands = new HashMap<>();
    private ServiceFactory serviceFactory;


    @Override
    public void init() throws ServletException {
        ApplicationFactory appFactory = null;
        try {
            appFactory = new ApplicationFactory();
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        serviceFactory = appFactory.getServiceFactory();
        initializeCommands();
    }

    private void initializeCommands() throws ServletException {
        try {
            // Bills
            commands.put("CREATE_BILL", serviceFactory.createAddBillCommand());
            commands.put("UPDATE_BILL", serviceFactory.createUpdateBillCommand());
            commands.put("DELETE_BILL", serviceFactory.createDeleteBillCommand());
            commands.put("UPDATE_BILL_STATUS", serviceFactory.createUpdateBillStatusCommand());
            commands.put("GET_ALL_BILLS", serviceFactory.createGetAllBillQuery());
            commands.put("GET_BILL_BY_ID", serviceFactory.createGetBillByIdQuery());
            commands.put("GET_BILLS_BY_PRODUCT", serviceFactory.createGetBillsByProductQuery());
            commands.put("GET_BILLS_BY_RETAILER", serviceFactory.createGetBillsByRetailerQuery());
            commands.put("GET_BILLS_BY_SUPPLIER", serviceFactory.createGetBillsBySupplierQuery());

            // Products
            commands.put("CREATE_PRODUCT", serviceFactory.createAddProductCommand());
            commands.put("UPDATE_PRODUCT", serviceFactory.createUpdateProductCommand());
            commands.put("DELETE_PRODUCT", serviceFactory.createDeleteProductCommand());
            commands.put("GET_ALL_PRODUCTS", serviceFactory.createGetAllProductQuery());
            commands.put("GET_PRODUCT_BY_ID", serviceFactory.createGetProductByIdQuery());
            commands.put("GET_PRODUCTS_BY_SUPPLIER", serviceFactory.createGetProductsBySupplierQuery());

            // Retailers
            commands.put("CREATE_RETAILER", serviceFactory.createAddRetailerCommand());
            commands.put("UPDATE_RETAILER", serviceFactory.createUpdateRetailerCommand());
            commands.put("DELETE_RETAILER", serviceFactory.createDeleteRetailerCommand());
            commands.put("GET_ALL_RETAILERS", serviceFactory.createGetAllRetailerQuery());
            commands.put("GET_RETAILER_BY_ID", serviceFactory.createGetRetailerByIdQuery());
            commands.put("GET_RETAILER_BY_NAME", serviceFactory.createGetRetailerByNameQuery());

            // Suppliers
            commands.put("CREATE_SUPPLIER", serviceFactory.createAddSupplierCommand());
            commands.put("UPDATE_SUPPLIER", serviceFactory.createUpdateSupplierCommand());
            commands.put("DELETE_SUPPLIER", serviceFactory.createDeleteSupplierCommand());
            commands.put("GET_ALL_SUPPLIERS", serviceFactory.createGetAllSupplierQuery());
            commands.put("GET_SUPPLIER_BY_ID", serviceFactory.createGetSupplierByIdQuery());
            commands.put("GET_SUPPLIER_BY_NAME", serviceFactory.createGetSupplierByNameQuery());

            // Images
            commands.put("ADD_IMAGE", serviceFactory.createAddImageCommand());
            commands.put("DELETE_IMAGE", serviceFactory.createDeleteImageCommand());
            commands.put("GET_IMAGE_BY_PRODUCT_ID", serviceFactory.createGetByProductIdImageQuery());


        } catch (Exception e) {
            throw new ServletException("Error initializing commands", e);
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

        String action = request.getParameter("action");
        AbstractCommand command = commands.get(action);

        if (command == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + action);
            return;
        }

        try {
            String view = command.execute(request);
            if (view != null) {
                RequestDispatcher dispatcher = request.getRequestDispatcher(view);
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}