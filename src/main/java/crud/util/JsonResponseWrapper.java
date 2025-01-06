package crud.util;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JsonResponseWrapper {
    private static final Gson gson = new Gson();

    public static void writeJsonResponse(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = gson.toJson(data);
        response.getWriter().write(jsonResponse);
    }

    public static void writeJsonError(HttpServletResponse response, String message, int statusCode) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);

        ErrorResponse error = new ErrorResponse(message);
        String jsonResponse = gson.toJson(error);
        response.getWriter().write(jsonResponse);
    }

    private static class ErrorResponse {
        private final String error;

        public ErrorResponse(String message) {
            this.error = message;
        }
    }
}
