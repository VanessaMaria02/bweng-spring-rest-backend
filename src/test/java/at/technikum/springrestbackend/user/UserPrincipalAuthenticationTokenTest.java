package at.technikum.springrestbackend.user;

import at.technikum.springrestbackend.security.user.CustomUserDetailsService;
import at.technikum.springrestbackend.security.user.UserPrincipal;
import at.technikum.springrestbackend.security.user.UserPrincipalAuthenticationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class UserPrincipalAuthenticationTokenTest {

    @Mock
     private UserPrincipal userPrincipal;
    @Mock
    private UserPrincipalAuthenticationToken userPrincipalAuthenticationToken;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void constructorTest() {
        // Mock UserPrincipal
        UserPrincipal userPrincipal = mock(UserPrincipal.class);

        // Create UserPrincipalAuthenticationToken
        UserPrincipalAuthenticationToken authenticationToken = new UserPrincipalAuthenticationToken(userPrincipal);

        // Verify that getAuthorities returns an empty collection (null is replaced with an empty collection)
        Collection<? extends GrantedAuthority> authorities = authenticationToken.getAuthorities();
        assertEquals(0, authorities.size());

        // Verify that getCredentials returns null
        assertEquals(null, authenticationToken.getCredentials());

        // Verify that getPrincipal returns the provided UserPrincipal
        assertEquals(userPrincipal, authenticationToken.getPrincipal());

        // Verify that isAuthenticated returns true
        assertEquals(true, authenticationToken.isAuthenticated());
    }
}

