package crud.util;
import crud.exception.BusinessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class PasswordUtils {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Hash the password
    public static String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    // Verify the password
    public static boolean verifyPassword(String plainPassword, String hashedPassword) throws BusinessException {
        boolean isPasswordAMatch = passwordEncoder.matches(plainPassword, hashedPassword);

        if (!isPasswordAMatch) {
            throw new BusinessException("The password does not match");
        }

        return isPasswordAMatch;
    }

}
