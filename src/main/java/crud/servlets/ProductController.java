package crud.servlets;

import crud.base.BaseController;
import crud.exception.DAOException;
import crud.infrastructure.ServiceFactory;
import jakarta.servlet.ServletException;


public class ProductController extends BaseController {


    public ProductController() throws DAOException, ServletException{
        init();
    }

    @Override
    protected void initializeCommands() throws ServletException{
        try{

            commands.put("create", serviceFactory.createAddProductCommand());
            commands.put("update", serviceFactory.createUpdateProductCommand());
            commands.put("delete", serviceFactory.createDeleteProductCommand());
            commands.put("get_all", serviceFactory.createGetAllProductQuery());
            commands.put("get_by_id", serviceFactory.createGetProductByIdQuery());
            commands.put("get_by_supplier", serviceFactory.createGetProductsBySupplierQuery());

        }catch (Exception ex){
            throw new ServletException("Error initializing commands", ex);
        }
    }
}
