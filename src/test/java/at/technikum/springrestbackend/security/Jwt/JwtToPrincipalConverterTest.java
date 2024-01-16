package at.technikum.springrestbackend.security.Jwt;

import at.technikum.springrestbackend.security.JwtToPrincipalConverter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class JwtToPrincipalConverterTest {


    private JwtToPrincipalConverter jwtToPrincipalConverter;

    @Mock
    private DecodedJWT decodedJWT;

    @Mock
    private Claim usernameClaim;

    @Mock
    private Claim roleClaim;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtToPrincipalConverter = new JwtToPrincipalConverter();

        when(decodedJWT.getClaim("username")).thenReturn(usernameClaim);
        when(decodedJWT.getClaim("role")).thenReturn(roleClaim);
        when(decodedJWT.getSubject()).thenReturn(UUID.randomUUID().toString());



        when(usernameClaim.asString()).thenReturn("testuser");
        when(roleClaim.asString()).thenReturn("user");
    }

    @Test
    public void testConvertJwtToPrincipal() {
        jwtToPrincipalConverter.convert(decodedJWT);

        assertEquals("testuser", JwtToPrincipalConverter.getCurrentUsername());
        assertEquals("user", JwtToPrincipalConverter.getCurrentUserRole());
    }
}
