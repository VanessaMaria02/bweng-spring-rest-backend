package at.technikum.springrestbackend.user;

import at.technikum.springrestbackend.model.User;
import at.technikum.springrestbackend.repository.UserRepository;
import at.technikum.springrestbackend.security.user.CustomUserDetailsService;
import at.technikum.springrestbackend.security.user.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test
    public void testLoadUserByUsername_UserFound_ShouldReturnUserDetails() {
        // Arrange
        String username = "testUser";
        User user = new User(UUID.randomUUID(), "testUser", "password", "ROLE_USER");
        when(userRepository.findByUsername(username)).thenReturn(user);

        // Act
        UserPrincipal userDetails = (UserPrincipal) userDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals(user.getRole(), userDetails.getAuthorities().iterator().next().getAuthority());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testLoadUserByUsername_UserNotFound_ShouldThrowUsernameNotFoundException() {
        // Arrange
        String username = "nonExistingUser";
        when(userRepository.findByUsername(username)).thenReturn(null);

        // Act and Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(username)
        );

        assertEquals("User not found with username: " + username, exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);
    }
}
