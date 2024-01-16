package at.technikum.springrestbackend.property;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(properties = {"security.jwt.secret=mockSecretValue"})
public class JwtPropertiesTest {

    @Autowired
    private JwtProperties jwtProperties;

    @Test
    public void jwtSecretPropertyShouldBeLoaded() {
        assertEquals("mockSecretValue", jwtProperties.getSecret());
    }
}
