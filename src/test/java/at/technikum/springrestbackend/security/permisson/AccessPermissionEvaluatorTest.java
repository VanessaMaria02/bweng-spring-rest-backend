package at.technikum.springrestbackend.security.permisson;

import at.technikum.springrestbackend.security.permission.AccessPermission;
import at.technikum.springrestbackend.security.permission.AccessPermissionEvaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class AccessPermissionEvaluatorTest {

    private AccessPermissionEvaluator accessPermissionEvaluator;

    @Mock
    private List<AccessPermission> accessPermissions;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        accessPermissionEvaluator = new AccessPermissionEvaluator(accessPermissions);
    }

    @Test
    public void hasPermission_WithSupportedAccessPermission_ShouldReturnTrue() {
        String targetType = "ExampleType";
        Serializable targetId = UUID.randomUUID();

        AccessPermission supportedAccessPermission = mock(AccessPermission.class);
        when(supportedAccessPermission.supports(authentication, targetType)).thenReturn(true);
        when(supportedAccessPermission.hasPermission(authentication, (UUID) targetId)).thenReturn(true);

        when(accessPermissions.iterator()).thenReturn(new ArrayList<AccessPermission>() {{
            add(supportedAccessPermission);
        }}.iterator());

        boolean result = accessPermissionEvaluator.hasPermission(authentication, targetId, targetType, "READ");

        assertTrue(result);
    }

    @Test
    public void hasPermission_WithUnsupportedAccessPermission_ShouldReturnFalse() {
        String targetType = "ExampleType";
        Serializable targetId = UUID.randomUUID();

        AccessPermission unsupportedAccessPermission = mock(AccessPermission.class);
        when(unsupportedAccessPermission.supports(authentication, targetType)).thenReturn(false);

        when(accessPermissions.iterator()).thenReturn(new ArrayList<AccessPermission>() {{
            add(unsupportedAccessPermission);
        }}.iterator());

        boolean result = accessPermissionEvaluator.hasPermission(authentication, targetId, targetType, "READ");

        assertFalse(result);
    }
    @Test
    public void hasPermission_AlwaysReturnsFalse_ShouldReturnFalse() {
        Object targetDomainObject = mock(Object.class);
        Object permission = mock(Object.class);

        boolean result = accessPermissionEvaluator.hasPermission(authentication, targetDomainObject, permission);

        assertFalse(result);
    }
}
