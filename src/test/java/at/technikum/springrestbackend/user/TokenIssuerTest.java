package at.technikum.springrestbackend.user;

import at.technikum.springrestbackend.security.TokenIssuer;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TokenIssuerTest {

    @Autowired
    private TokenIssuer tokenIssuer;

    @Mock
    private TokenIssuer mockTokenIssuer;

    @Test
    public void testIssueTokenWithMockData() {

        UUID userId = UUID.randomUUID();
        String username = "testUser";
        String role = "ROLE_USER";

        when(mockTokenIssuer.issue(userId, username, role)).thenReturn("mockedToken123");

        String token = mockTokenIssuer.issue(userId, username, role);

        assertNotNull(token);
    }
}

