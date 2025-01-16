package crud.servlets;

import crud.base.BaseController;
import crud.exception.DAOException;
import jakarta.servlet.ServletException;

public class SupplierController extends BaseController {

    public SupplierController() throws DAOException, ServletException{
        init();
    }

    @Override
    protected void initializeCommands() throws ServletException {

        try{
            commands.put("create", serviceFactory.createAddSupplierCommand());
            commands.put("update", serviceFactory.createUpdateSupplierCommand());
            commands.put("delete", serviceFactory.createDeleteSupplierCommand());
            commands.put("get_all", serviceFactory.createGetAllSupplierQuery());
            commands.put("get_by_id", serviceFactory.createGetSupplierByIdQuery());
            commands.put("get_by_name", serviceFactory.createGetSupplierByNameQuery());

        } catch (Exception ex){
            throw new ServletException("Error initializing commands", ex);
        }
    }
}
