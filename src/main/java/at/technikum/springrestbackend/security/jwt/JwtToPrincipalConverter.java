package at.technikum.springrestbackend.security.jwt;

import at.technikum.springrestbackend.security.user.UserPrincipal;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;


import java.util.UUID;

@Component
public class JwtToPrincipalConverter {

    public UserPrincipal convert(DecodedJWT jwt) {
        return new UserPrincipal(
                UUID.fromString(jwt.getSubject()),
                jwt.getClaim("username").asString(),
                "",
                jwt.getClaim("role").asString()
        );
    }
}
