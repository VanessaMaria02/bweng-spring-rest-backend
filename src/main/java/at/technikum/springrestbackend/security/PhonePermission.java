package at.technikum.springrestbackend.security.permission;

import at.technikum.springrestbackend.model.Phone;
import at.technikum.springrestbackend.security.user.UserPrincipal;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public class PhonePermission implements AccessPermission {
    @Override
    public boolean supports(Authentication authentication, String className) {
        return className.equals(Phone.class.getName());
    }

    @Override
    public boolean hasPermission(Authentication authentication, UUID resourceId) {
        return ((UserPrincipal) authentication.getPrincipal()).getId().equals(resourceId);
    }
}
