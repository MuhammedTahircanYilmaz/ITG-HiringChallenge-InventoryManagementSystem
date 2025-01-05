package crud.authorization.login;

import java.util.UUID;

public class LoginResponse {
    private  final String token;
    private final UUID userId;
    private final String email;
    private final String userType;

    public LoginResponse(String token, UUID userId, String email, String userType) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.userType = userType;
    }

    public String getToken() {
        return token;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getUserType() {
        return userType;
    }
}

