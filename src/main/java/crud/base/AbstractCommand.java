package crud.base;

//import org.apache.log4j.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

public abstract class AbstractCommand implements BaseService {

 //   private static final Logger logger = Logger.getLogger(AbstractCommand.class);

    private static final String ENTITY_KEY = "entity";
    private static final String ENTITY_LIST_KEY = "entityList";

    private static final String SUCCESS_MESSAGE_KEY = "msgSuccess";
    private static final String INFO_MESSAGE_KEY = "msgInfo";
    private static final String WARN_MESSAGE_KEY = "msgWarn";
    private static final String ERROR_MESSAGE_KEY = "msgError";

    private static final String UNEXPECTED_ERROR_ATTRIBUTE_MESSAGE = "Sorry, an unexpected error occurred while retrieving the requested attribute.";

    protected static final String ADD_SUCCESS_MESSAGE = "Saved successfully!";
    protected static final String UPDATE_SUCCESS_MESSAGE = "Updated successfully!";
    protected static final String DELETE_SUCCESS_MESSAGE = "Deleted successfully!";

    protected static final String PAGE_SUPPLIER_LIST = "/supplier/list.jsp";
    protected static final String PAGE_SUPPLIER_FORM = "/supplier/form.jsp";
    protected static final String PAGE_RETAILER_LIST = "/retailer/list.jsp";
    protected static final String PAGE_RETAILER_FORM = "/retailer/form.jsp";
    protected static final String PAGE_BILL_LIST = "/bill/list.jsp";
    protected static final String PAGE_BILL_FORM = "/bill/form.jsp";
    protected static final String PAGE_PRODUCT_LIST = "/product/list.jsp";
    protected static final String PAGE_PRODUCT_FORM = "/product/form.jsp";

    protected UUID getId(HttpServletRequest request) throws ServletException {
        try {
            String id = request.getParameter("id");
            return UUID.fromString(id);
        } catch (Exception e) {
            setAttributeException(request, e);
            throw new ServletException(UNEXPECTED_ERROR_ATTRIBUTE_MESSAGE, e);
        }
    }

    protected String getKeyword(HttpServletRequest request) throws ServletException {
        try {
            return request.getParameter("keyword");
        } catch (Exception ex) {
            setAttributeException(request, ex);
            throw new ServletException(UNEXPECTED_ERROR_ATTRIBUTE_MESSAGE, ex);
        }
    }

    protected void addSuccessMessage(HttpServletRequest request, String message) {
        request.setAttribute(SUCCESS_MESSAGE_KEY, message);
    }

    protected void addInfoMessage(HttpServletRequest request, String message) {
        request.setAttribute(INFO_MESSAGE_KEY, message);
    }

    protected void addWarnMessage(HttpServletRequest request, String message) {
        request.setAttribute(WARN_MESSAGE_KEY, message);
    }

    private void addErrorMessage(HttpServletRequest request, String message) {
        request.setAttribute(ERROR_MESSAGE_KEY, message);
    }

    protected <T> void setEntities(HttpServletRequest request, List<T> entities, String emptyMessage) {
        if (entities == null || entities.isEmpty()) {
            addInfoMessage(request, emptyMessage);
        } else {
            request.setAttribute(ENTITY_LIST_KEY, entities);
        }
    }

    protected void setEntity(HttpServletRequest request, Object entity) {
        request.setAttribute(ENTITY_KEY, entity);
    }

    protected void setException(HttpServletRequest request, Exception exception) {
        addErrorMessage(request, exception.getMessage());
    }

    private void setAttributeException(HttpServletRequest request, Exception exception) {
        request.setAttribute(ERROR_MESSAGE_KEY, UNEXPECTED_ERROR_ATTRIBUTE_MESSAGE);
        //logger.error(UNEXPECTED_ERROR_ATTRIBUTE_MESSAGE, exception);
    }
}
