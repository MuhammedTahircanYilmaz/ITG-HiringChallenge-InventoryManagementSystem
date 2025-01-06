package crud.base;

public class ServiceResult {
    private final String view;
    private final Object data;
    private final boolean isJson;

    private ServiceResult(String view, Object data, boolean isJson) {
        this.view = view;
        this.data = data;
        this.isJson = isJson;
    }

    public static ServiceResult view(String view) {
        return new ServiceResult(view, null, false);
    }

    public static ServiceResult json(Object data) {
        return new ServiceResult(null, data, true);
    }

    public String getView() {
        return view;
    }

    public Object getData() {
        return data;
    }

    public boolean isJson() {
        return isJson;
    }
}