package at.technikum.springrestbackend.security;

import at.technikum.springrestbackend.security.user.UserPrincipal;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JwtToPrincipalConverter {

    private static final ThreadLocal<String> currentUsername = new ThreadLocal<>();
    private static final ThreadLocal<String> currentUserRole = new ThreadLocal<>();

    public UserPrincipal convert(DecodedJWT jwt) {
        String username = jwt.getClaim("username").asString();
        String userRole = jwt.getClaim("role").asString();

        currentUsername.set(username);
        currentUserRole.set(userRole);

        return new UserPrincipal(
                UUID.fromString(jwt.getSubject()),
                username,
                "",
                userRole
        );
    }

    public static String getCurrentUsername() {
        return currentUsername.get();
    }

    public static String getCurrentUserRole() {
        return currentUserRole.get();
    }

}
