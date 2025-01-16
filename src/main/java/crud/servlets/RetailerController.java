package crud.servlets;

import crud.base.BaseController;
import crud.exception.DAOException;
import jakarta.servlet.ServletException;

public class RetailerController extends BaseController {

    public RetailerController() throws DAOException, ServletException{
        init();
    }

    @Override
    protected void initializeCommands() throws ServletException {

        try{
            commands.put("create", serviceFactory.createAddRetailerCommand());
            commands.put("update", serviceFactory.createUpdateRetailerCommand());
            commands.put("delete", serviceFactory.createDeleteRetailerCommand());
            commands.put("get_all", serviceFactory.createGetAllRetailerQuery());
            commands.put("get_by_id", serviceFactory.createGetRetailerByIdQuery());
            commands.put("get_by_name", serviceFactory.createGetRetailerByNameQuery());

        } catch (Exception ex){
            throw new ServletException("Error initializing commands", ex);
        }
    }
}
