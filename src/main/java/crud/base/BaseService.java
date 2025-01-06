package crud.base;

import jakarta.servlet.http.HttpServletRequest;

public interface BaseService {
    ServiceResult execute(HttpServletRequest request) throws Exception;
}