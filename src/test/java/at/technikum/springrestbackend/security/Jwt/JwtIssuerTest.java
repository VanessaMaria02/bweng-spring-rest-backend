package at.technikum.springrestbackend.security.Jwt;

import at.technikum.springrestbackend.property.JwtProperties;
import at.technikum.springrestbackend.security.JwtIssuer;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class JwtIssuerTest {


    private JwtIssuer jwtIssuer;

    @Mock
    private JwtProperties jwtProperties;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtIssuer = new JwtIssuer(jwtProperties);
        when(jwtProperties.getSecret()).thenReturn("your_secret_key_here"); // Ersetzen Sie durch Ihren geheimen Schlüssel
    }

    @Test
    public void testIssueToken() {
        UUID userId = UUID.randomUUID();
        String username = "testuser";
        String role = "user";

        String token = jwtIssuer.issue(userId, username, role);

        // Decodieren und überprüfen Sie das Token
        DecodedJWT decodedJWT = JWT.decode(token);
        assertEquals(String.valueOf(userId), decodedJWT.getSubject());
        assertEquals(username, decodedJWT.getClaim("username").asString());
        assertEquals(role, decodedJWT.getClaim("role").asString());

        // Überprüfen Sie das Ablaufdatum (ExpiresAt) auf Gültigkeit (zum Beispiel, ob es in den nächsten 2 Stunden liegt)
        Instant expiresAt = decodedJWT.getExpiresAt().toInstant();
        Instant now = Instant.now();
        Instant expectedExpiresAt = now.plus(2, ChronoUnit.HOURS);
        assertEquals(expectedExpiresAt.truncatedTo(ChronoUnit.SECONDS), expiresAt.truncatedTo(ChronoUnit.SECONDS));
    }
}

