package crud.servlets;

import crud.base.BaseController;
import crud.exception.DAOException;

import jakarta.servlet.ServletException;


public class BillController extends BaseController {

    public BillController() throws DAOException, ServletException{
        init();
    }

    @Override
    protected void initializeCommands() throws ServletException {
        try {
            commands.put("create", serviceFactory.createAddBillCommand());
            commands.put("update", serviceFactory.createUpdateBillCommand());
            commands.put("delete", serviceFactory.createDeleteBillCommand());
            commands.put("update_status", serviceFactory.createUpdateBillStatusCommand());
            commands.put("all", serviceFactory.createGetAllBillQuery());
            commands.put("get_by_id", serviceFactory.createGetBillByIdQuery());
            commands.put("get_by_product", serviceFactory.createGetBillsByProductQuery());
            commands.put("get_by_retailer", serviceFactory.createGetBillsByRetailerQuery());
            commands.put("get_by_supplier", serviceFactory.createGetBillsBySupplierQuery());
        } catch (Exception ex) {
            throw new ServletException("Error initializing commands", ex);
        }
    }
}

