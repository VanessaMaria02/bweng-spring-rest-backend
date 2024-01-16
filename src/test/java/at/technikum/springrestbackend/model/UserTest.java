package at.technikum.springrestbackend.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {


    @Test
    public void testUserConstructorWithAllParameters() {
        User user = new User("testuser", "password123", "user", "John", "Doe", "Mr.", "test@example.com", "profile.jpg", true, "123 Street", "City", 12345, "1A", "US");

        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("user", user.getRole());
        // Add assertions for other fields as needed
    }

    @Test
    public void testUserConstructorWithId() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "testuser", "password123", "user");

        assertNotNull(user);
        assertEquals(id, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("user", user.getRole());
    }
    @Test
    public void testGettersAndSetters() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setRole("user");
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setSalutation("Mr.");
        user.setEmail("test@example.com");
        user.setCountryCode("US");
        user.setPostalCode(12345);
        user.setCity("City");
        user.setStreet("123 Street");
        user.setHouseNumber("1A");
        user.setProfilePicture("profile.jpg");
        user.setStatus(true);

        assertEquals("testuser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("user", user.getRole());
        assertEquals("John", user.getFirstname());
        assertEquals("Doe", user.getLastname());
        assertEquals("Mr.", user.getSalutation());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("US", user.getCountryCode());
        assertEquals(12345, user.getPostalCode());
        assertEquals("City", user.getCity());
        assertEquals("123 Street", user.getStreet());
        assertEquals("1A", user.getHouseNumber());
        assertEquals("profile.jpg", user.getProfilePicture());
        assertTrue(user.getStatus());
    }
    @Test
    public void testCreatedOnAndLastUpdatedOn() {
        Instant now = Instant.now();
        User user = new User();

        // Set createdOn and lastUpdatedOn to the current timestamp
        user.setCreatedOn(now);
        user.setLastUpdatedOn(now);

        assertEquals(now, user.getCreatedOn());
        assertEquals(now, user.getLastUpdatedOn());
    }
}

