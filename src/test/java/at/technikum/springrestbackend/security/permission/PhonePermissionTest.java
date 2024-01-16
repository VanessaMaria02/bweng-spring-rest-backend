package at.technikum.springrestbackend.security.permission;

import at.technikum.springrestbackend.model.Phone;
import at.technikum.springrestbackend.security.permission.AccessPermission;
import at.technikum.springrestbackend.security.permission.PhonePermission;
import at.technikum.springrestbackend.security.user.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class PhonePermissionTest {

    private final AccessPermission phonePermission = new PhonePermission();

    @Test
    public void supports_ReturnsTrueForPhoneClass_ShouldReturnTrue() {
        Authentication authentication = createAuthentication("USER");
        boolean result = phonePermission.supports(authentication, Phone.class.getName());
        assertTrue(result);
    }

    @Test
    public void supports_ReturnsFalseForNonPhoneClass_ShouldReturnFalse() {
        Authentication authentication = createAuthentication("USER");
        boolean result = phonePermission.supports(authentication, "SomeOtherClass");
        assertFalse(result);
    }

    @Test
    public void hasPermission_ReturnsTrueForMatchingId_ShouldReturnTrue() {
        UUID resourceId = UUID.randomUUID();
        Authentication authentication = createAuthenticationWithId("USER", resourceId);

        boolean result = phonePermission.hasPermission(authentication, resourceId);
        assertTrue(result);
    }

    @Test
    public void hasPermission_ReturnsFalseForNonMatchingId_ShouldReturnFalse() {
        UUID resourceId = UUID.randomUUID();
        UUID differentId = UUID.randomUUID();
        Authentication authentication = createAuthenticationWithId("USER", differentId);

        boolean result = phonePermission.hasPermission(authentication, resourceId);
        assertFalse(result);
    }

    private Authentication createAuthentication(String role) {
        UserPrincipal userPrincipal = new UserPrincipal(UUID.randomUUID(), "username", "password", role);
        return mockAuthentication(userPrincipal);
    }

    private Authentication createAuthenticationWithId(String role, UUID id) {
        UserPrincipal userPrincipal = new UserPrincipal(id, "username", "password", role);
        return mockAuthentication(userPrincipal);
    }

    private Authentication mockAuthentication(UserPrincipal userPrincipal) {
        Authentication authentication = mock(Authentication.class);
        userPrincipal.getAuthorities().forEach(authority ->
                assertTrue(authority instanceof SimpleGrantedAuthority)
        );
        Mockito.when(authentication.getPrincipal()).thenReturn(userPrincipal);
        return authentication;
    }
}
