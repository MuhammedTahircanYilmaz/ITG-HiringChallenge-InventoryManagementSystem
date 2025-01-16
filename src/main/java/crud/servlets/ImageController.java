package crud.servlets;

import crud.base.BaseController;
import crud.exception.DAOException;
import crud.infrastructure.ServiceFactory;
import jakarta.servlet.ServletException;

public class ImageController extends BaseController{

        public ImageController() throws DAOException, ServletException {
            init();
        }

        @Override
        protected void initializeCommands() throws ServletException{
            try{

                commands.put("images/add", serviceFactory.createAddImageCommand());
                commands.put("images/delete", serviceFactory.createDeleteImageCommand());
                commands.put("images/get_by_product", serviceFactory.createGetByProductIdImageQuery());

            }catch (Exception ex){
                throw new ServletException("Error initializing commands", ex);
            }
        }
    }