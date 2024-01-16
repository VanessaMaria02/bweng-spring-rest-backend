package at.technikum.springrestbackend.security;

import at.technikum.springrestbackend.property.JwtProperties;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtDecoder {
    private final JwtProperties jwtProperties;

    public DecodedJWT decode(String token) throws TokenExpiredException {
        try {
            // Attempt to decode and verify the token
            return JWT.require(Algorithm.HMAC256(jwtProperties.getSecret())).build().verify(token);
        } catch (TokenExpiredException e) {
            // Token has expired, rethrow the exception or handle it as needed
            throw e;
        }
    }
}
