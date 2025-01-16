package crud.base;

import crud.exception.DAOException;
import crud.infrastructure.ApplicationFactory;
import crud.infrastructure.ServiceFactory;
import crud.util.Logger;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseController {
    public final Map<String, AbstractCommand> commands = new HashMap<>();
    protected ServiceFactory serviceFactory;

    public void init() throws ServletException{
        ApplicationFactory applicationFactory = null;
        try{
            applicationFactory = new ApplicationFactory();
        }catch (DAOException ex){
            Logger.error(this.getClass().getName(), ex.getMessage());
        }
        serviceFactory = applicationFactory.getServiceFactory();
        initializeCommands();
    }

    protected void initializeCommands() throws ServletException
    {

    }

    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request path");
            return;
        }

        String action = pathInfo.substring(pathInfo.lastIndexOf("/") + 1);

        try{
            AbstractCommand command = commands.get(action);

            if (command == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + action);
                return;
            }

            try {
                if ("application/json".equals(request.getContentType())) {
                    String jsonPayload = readJsonBody(request);
                    request.setAttribute("jsonBody", jsonPayload); // Store in request attributes
                }

                try {
                    String view = command.execute(request).getView();
                    if (view != null) {
                        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
                        dispatcher.forward(request, response);
                    }
                } catch (Exception ex) {
                    throw new ServletException(ex);
                }

            } catch (Exception ex){
                Logger.error(this.getClass().getName(), ex.getMessage());
            }
        }catch (Exception ex){
            Logger.error(this.getClass().getName(), ex.getMessage());
        }
    }

    private String readJsonBody(HttpServletRequest request) throws IOException {
        StringBuilder jsonBuffer = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }

        return jsonBuffer.toString();
    }
}
