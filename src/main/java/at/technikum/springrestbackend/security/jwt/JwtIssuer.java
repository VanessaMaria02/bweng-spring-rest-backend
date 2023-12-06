package at.technikum.springrestbackend.security.jwt;

import at.technikum.springrestbackend.property.JwtProperties;
import at.technikum.springrestbackend.security.TokenIssuer;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class JwtIssuer implements TokenIssuer {
    private final JwtProperties jwtProperties;

    @Override
    public String issue(UUID userID, String username, String role){
        return JWT.create()
                .withSubject(String.valueOf(userID))
                .withExpiresAt(Instant.now().plus(Duration.of(4, ChronoUnit.HOURS)))
                .withClaim("username", username)
                .withClaim("role", role)
                .sign(Algorithm.HMAC256(jwtProperties.getSecret()));
    }
}
