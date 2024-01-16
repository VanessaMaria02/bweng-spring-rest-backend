package at.technikum.springrestbackend.util;

import at.technikum.springrestbackend.model.User;
import at.technikum.springrestbackend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserValidatorTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserValidator userValidator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void validateUserRegistrationShouldReturnErrorsForTakenUsername() {
        User user = new User();
        user.setUsername("existingUser");
        user.setEmail("new@example.com");
        user.setPassword("ValidPassword123");
        user.setCountryCode("US");

        when(userService.isUsernameTaken(user.getUsername())).thenReturn(true);
        when(userService.isEmailTaken(user.getEmail())).thenReturn(false);

        List<String> validationErrors = userValidator.validateUserRegistration(user);

        assertFalse(validationErrors.isEmpty());
        assertTrue(validationErrors.contains("Username is already taken"));
    }

    @Test
    public void validateUserRegistrationShouldReturnErrorsForTakenEmail() {
        User user = new User();
        user.setUsername("newUser");
        user.setEmail("existing@example.com");
        user.setPassword("ValidPassword123");
        user.setCountryCode("US");

        when(userService.isUsernameTaken(user.getUsername())).thenReturn(false);
        when(userService.isEmailTaken(user.getEmail())).thenReturn(true);

        List<String> validationErrors = userValidator.validateUserRegistration(user);

        assertFalse(validationErrors.isEmpty());
        assertTrue(validationErrors.contains("Email is already taken"));
    }

    @Test
    public void validateUserRegistrationShouldReturnErrorsForInvalidPassword() {
        User user = new User();
        user.setUsername("newUser");
        user.setEmail("new@example.com");
        user.setPassword("WeakPassword");
        user.setCountryCode("US");

        when(userService.isUsernameTaken(user.getUsername())).thenReturn(false);
        when(userService.isEmailTaken(user.getEmail())).thenReturn(false);

        List<String> validationErrors = userValidator.validateUserRegistration(user);

        assertFalse(validationErrors.isEmpty());
        assertTrue(validationErrors.contains("Invalid password"));
    }

    @Test
    public void validateUserRegistrationShouldReturnErrorsForInvalidCountryCode() {
        User user = new User();
        user.setUsername("newUser");
        user.setEmail("new@example.com");
        user.setPassword("ValidPassword123");
        user.setCountryCode("INVALID");

        when(userService.isUsernameTaken(user.getUsername())).thenReturn(false);
        when(userService.isEmailTaken(user.getEmail())).thenReturn(false);

        List<String> validationErrors = userValidator.validateUserRegistration(user);

        assertFalse(validationErrors.isEmpty());
        assertTrue(validationErrors.contains("Invalid CountryCode"));
    }
}
