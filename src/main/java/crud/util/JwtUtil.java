package crud.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;
import java.util.UUID;

public class JwtUtil {
    private static final String SECRET = "SuperSecretTokenTokenSecretSuperSecretsecretsupertoken";
    private static final String ISSUER = "itghiringchallenge@itg.com";
    private static final long EXPIRATION_TIME = 3600000;

    public String generateToken(UUID id, String roleName) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        return JWT.create()
                .withIssuer(ISSUER)
                .withClaim("userId",id.toString())
                .withClaim("roleName",roleName)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
    }

    public DecodedJWT validateToken(String token) throws JWTVerificationException {
        try{
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();
            return verifier.verify(token);
        }   catch (JWTVerificationException ex) {
            Logger.error(this.getClass().getName(), ex.getMessage());
        }
        return null;
    }

    public String getUserId(String token) {
        return validateToken(token).getClaim("userId").asString();
    }
    public String getRoleName(String token) {
        return validateToken(token).getClaim("roleName").asString();
    }
}