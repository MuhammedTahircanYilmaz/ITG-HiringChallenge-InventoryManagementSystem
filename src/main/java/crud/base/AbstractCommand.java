package crud.base;

import crud.util.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCommand implements BaseService {
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

    protected static final String CREATE_PRODUCT = "/create-product.jsp";
    protected static final String CURRENT_PURCHASES = "/current-purchases.jsp";
    protected static final String LOGIN = "/login.jsp";
    protected static final String REGISTER = "/register.jsp";
    protected static final String PAST_PURCHASES = "/past-purchases.jsp";
    protected static final String PAST_SALES = "/past-sales.jsp";
    protected static final String PENDING_BILLS = "/pending_bills.jsp";
    protected static final String PRODUCT = "/product.jsp";
    protected static final String PROFILE = "/profile.jsp";
    protected static final String RETAILER_MAIN = "/retailer-main.jsp";
    protected static final String SUPPLIER_MAIN = "/supplier-main.jsp";
    protected static final String SEARCH_RESULTS = "/search-results.jsp";
    protected static final String SUPPLIER_PRODUCTS = "/supplier-products.jsp";


    protected ServiceResult createView(String view) {
        return ServiceResult.view(view);
    }

    protected ServiceResult createJsonResponse(Object data) {
        return ServiceResult.json(data);
    }

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
        Logger.error(this.getClass().getName(), UNEXPECTED_ERROR_ATTRIBUTE_MESSAGE);
    }

    @Override
    public abstract ServiceResult execute(HttpServletRequest request) throws Exception;
}