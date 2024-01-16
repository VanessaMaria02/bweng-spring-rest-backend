package at.technikum.springrestbackend.security.Jwt;

import at.technikum.springrestbackend.property.JwtProperties;
import at.technikum.springrestbackend.security.JwtDecoder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtDecoderTest {


    @Mock
    private JwtProperties jwtProperties;

    @InjectMocks
    private JwtDecoder jwtDecoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDecode_ValidToken() {
        String secret = "handyshopsecretforjwt";
        String token = generateValidToken(secret);

        when(jwtProperties.getSecret()).thenReturn(secret);

        DecodedJWT decodedJWT = jwtDecoder.decode(token);

        assertNotNull(decodedJWT);
        // Add additional assertions based on your token content if needed
    }

    @Test
    void testDecode_ExpiredToken() {
        String secret = "handyshopsecretforjwt";
        String token = generateExpiredToken(secret);

        when(jwtProperties.getSecret()).thenReturn(secret);

        assertThrows(TokenExpiredException.class, () -> jwtDecoder.decode(token));
    }

    @Test
    void testDecode_InvalidToken() {
        String secret = "handyshopsecretforjwt";
        String token = "invalid_token";

        when(jwtProperties.getSecret()).thenReturn(secret);

        assertThrows(Exception.class, () -> jwtDecoder.decode(token));
    }

    private String generateValidToken(String secret) {
        return JWT.create()
                .withClaim("key", "value") // Add claims as needed
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000)) // Expires in 1 hour
                .sign(Algorithm.HMAC256(secret));
    }

    private String generateExpiredToken(String secret) {
        return JWT.create()
                .withClaim("key", "value") // Add claims as needed
                .withExpiresAt(new Date(System.currentTimeMillis() - 3600000)) // Expired 1 hour ago
                .sign(Algorithm.HMAC256(secret));
    }
}


