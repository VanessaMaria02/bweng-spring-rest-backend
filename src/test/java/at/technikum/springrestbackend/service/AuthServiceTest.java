package at.technikum.springrestbackend.service;

import at.technikum.springrestbackend.dto.TokenRequest;
import at.technikum.springrestbackend.dto.TokenResponse;
import at.technikum.springrestbackend.security.TokenIssuer;
import at.technikum.springrestbackend.security.user.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    @Mock
    private TokenIssuer tokenIssuer;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void authenticateShouldReturnTokenResponse() {
        // Set up mock data
        UUID userId = UUID.randomUUID();
        String username = "user";
        String password = "password";
        String role = "ROLE_USER";
        String token = "mockToken";
        UserPrincipal principal = new UserPrincipal(userId, username, password, role);
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, password, principal.getAuthorities());

        // Configure mocks
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(tokenIssuer.issue(any(UUID.class), anyString(), anyString())).thenReturn(token);

        // Call the method to test
        TokenResponse tokenResponse = authService.authenticate(new TokenRequest(username, password));
        // Verify results
        assertNotNull(tokenResponse);
        assertEquals(token, tokenResponse.getToken());
    }
}
