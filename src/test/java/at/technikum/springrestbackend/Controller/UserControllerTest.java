package at.technikum.springrestbackend.Controller;
import at.technikum.springrestbackend.controller.UserController;
import at.technikum.springrestbackend.model.User;
import at.technikum.springrestbackend.service.UserService;
import at.technikum.springrestbackend.util.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private UserController userController;
    private User testUser;
    private UUID testUserId;

    private Instant instant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetUsers() {
        // Mock the behavior of the UserService to return a list of users
        List<User> mockUsers = Arrays.asList(
                new User("john_doe","P@ssw0rd", "ROLE_user", "john", "doe", "Mr","john@example.com", null, true, "Am lange Felde", "Wien", 1220, "59/2/3","AT"),
                new User("john_doe2","P@ssw0rd", "ROLE_user", "john", "doe", "Mr","john2@example.com", null, true, "Am lange Felde", "Wien", 1220, "59/2/3","AT")
        );
        when(userService.getUsers()).thenReturn(mockUsers);

        // Call the getUsers method on the UserController
        List<User> result = userController.getUsers();

        // Verify that the result matches the expected list of users
        assertEquals(mockUsers, result);
    }

    @Test
    void testGetUserRole_UserExists() {
        // Mock the behavior of the UserService to return a user with a specific role
        User mockUser = new User("john_doe","P@ssw0rd", "ROLE_user", "john", "doe", "Mr","john@example.com", null, true, "Am lange Felde", "Wien", 1220, "59/2/3","AT");
        when(userService.getUserByUsername("john_doe")).thenReturn(mockUser);

        // Call the getUserRole method on the UserController
        String result = userController.getUserRole("john_doe");

        // Verify that the result matches the expected role
        assertEquals("ROLE_user", result);
    }

    @Test
    void testGetUserRole_UserNotFound() {
        // Mock the behavior of the UserService to return null (user not found)
        when(userService.getUserByUsername("nonexistentUser")).thenReturn(null);

        // Call the getUserRole method on the UserController
        String result = userController.getUserRole("nonexistentUser");

        // Verify that the result is the expected message for a user not found
        assertEquals("No user with this name found", result);
    }

    @Test
    void testHandleUserCreation_ValidUser() {
        // Mock the behavior of the UserValidator to return an empty list (no validation errors)
        when(userValidator.validateUserRegistration(any(User.class))).thenReturn(Arrays.asList());

        // Mock the behavior of the UserService to successfully create a user
        when(userService.createUser(any(User.class))).thenReturn(new User());

        // Call the handleUserCreation method on the UserController with a valid user
        ResponseEntity<Object> result = userController.registerUser(new User("john_doe","P@ssw0rd", "ROLE_user", "john", "doe", "Mr","john@example.com", null, true, "Am lange Felde", "Wien", 1220, "59/2/3","AT"));

        // Verify that the result is an OK response
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testHandleUserCreation_InvalidUser() {
        // Mock the behavior of the UserValidator to return a list of validation errors
        when(userValidator.validateUserRegistration(any(User.class))).thenReturn(Arrays.asList("Error 1", "Error 2"));

        // Call the handleUserCreation method on the UserController with an invalid user
        ResponseEntity<Object> result = userController.registerUser(new User());

        // Verify that the result is a BAD_REQUEST response with the expected validation errors
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(Arrays.asList("Error 1", "Error 2"), result.getBody());
    }

    @Test
    void testHandleUserCreation_TokenExpiredException() {
        // Mock the behavior of the UserValidator to return an empty list (no validation errors)
        when(userValidator.validateUserRegistration(any(User.class))).thenReturn(Arrays.asList());

        // Mock the behavior of the UserService to throw TokenExpiredException
        when(userService.createUser(any(User.class))).thenThrow(new TokenExpiredException("Expired", instant));

        // Call the handleUserCreation method on the UserController with a valid user
        ResponseEntity<Object> result = userController.registerUser(new User("john_doe", "P@ssw0rd", "ROLE_user", "john", "doe", "Mr", "john@example.com", null, true, "Am lange Felde", "Wien", 1220, "59/2/3", "AT"));

        // Verify that the result is a UNAUTHORIZED response for TokenExpiredException
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("The JWT Token is expired, pleas login in again", result.getBody());
    }

    @Test
    void testHandleUserCreation_GenericException() {
        // Mock the behavior of the UserValidator to return an empty list (no validation errors)
        when(userValidator.validateUserRegistration(any(User.class))).thenReturn(Arrays.asList());

        // Mock the behavior of the UserService to throw a generic exception
        when(userService.createUser(any(User.class))).thenThrow(new RuntimeException("Some error"));

        // Call the handleUserCreation method on the UserController with a valid user
        ResponseEntity<Object> result = userController.registerUser(new User("john_doe", "P@ssw0rd", "ROLE_user", "john", "doe", "Mr", "john@example.com", null, true, "Am lange Felde", "Wien", 1220, "59/2/3", "AT"));

        // Verify that the result is an INTERNAL_SERVER_ERROR response for a generic exception
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("An error occurred while processing your request.", result.getBody());
    }

    @Test
    void testHandleUserDeletion_UserNotFound() {
        // Mock the behavior of the UserService to return null when calling getUser
        when(userService.getUser(any(UUID.class))).thenReturn(null);

        // Call the handleUserDeletion method on the UserController
        ResponseEntity<Object> result = userController.deleteUser(UUID.randomUUID());

        // Verify that the result is a NOT_FOUND response
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testHandleUserDeletion_ValidUser() {

        UUID mockUserId = UUID.randomUUID();
        User mockUser = new User("valid_user", "P@ssw0rd", "ROLE_user", "Jane", "Doe", "Ms", "jane@example.com", null, true, "Some Address", "City", 1000, "1/2/3", "AT");
        when(userService.getUser(mockUserId)).thenReturn(mockUser);

        doNothing().when(userService).deleteUser(mockUserId);

        ResponseEntity<Object> result = userController.deleteUser(mockUserId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("User deleted successfully", result.getBody());
    }

    @Test
    void testDeleteUser_TokenExpiredException() {
        UUID mockUserId = UUID.randomUUID();

        when(userService.getUser(mockUserId)).thenThrow(new TokenExpiredException("Expired", Instant.now()));

        ResponseEntity<Object> result = userController.deleteUser(mockUserId);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("The JWT Token is expired, pleas login in again", result.getBody());
    }

    @Test
    void testDeleteUser_OtherException() {
        UUID mockUserId = UUID.randomUUID();

        when(userService.getUser(mockUserId)).thenThrow(new RuntimeException("Some error"));

        ResponseEntity<Object> result = userController.deleteUser(mockUserId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("An error occurred while processing your request.", result.getBody());
    }



    @Test
    void testGetUserById() {
        when(userService.getUser(testUserId)).thenReturn(testUser);
        assertEquals(testUser, userController.getUser(testUserId));
    }

    @Test
    void testRegisterUser_Invalid() {
        when(userValidator.validateUserRegistration(testUser)).thenReturn(List.of("Invalid Data"));
        ResponseEntity<Object> response = userController.registerUser(testUser);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userService.getUser(testUserId)).thenReturn(null);
        ResponseEntity<Object> response = userController.deleteUser(testUserId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    void testDeleteUser_Exception() {
        when(userService.getUser(testUserId)).thenThrow(new TokenExpiredException("Expired", Instant.now()));
        ResponseEntity<Object> response = userController.deleteUser(testUserId);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testGetUserByUsername_UserExists() {
        // Mock the behavior of the UserService to return a user with a specific username
        User mockUser = new User("john_doe","P@ssw0rd", "ROLE_user", "john", "doe", "Mr","john@example.com", null, true, "Am lange Felde", "Wien", 1220, "59/2/3","AT");
        when(userService.getUserByUsername("john_doe")).thenReturn(mockUser);

        // Call the getUserUserName method on the UserController
        User result = userController.getUserUserName("john_doe");

        // Verify that the result matches the expected user
        assertEquals(mockUser, result);
    }

    @Test
    void testGetUserByUsername_UserNotFound() {
        // Mock the behavior of the UserService to return null (user not found)
        when(userService.getUserByUsername("nonexistentUser")).thenReturn(null);

        // Call the getUserUserName method on the UserController
        User result = userController.getUserUserName("nonexistentUser");

        // Verify that the result is null for a user not found
        assertNull(result);
    }

    @Test
    void testGetUsersRole_UsersExist() {
        // Mock the behavior of the UserService to return a list of users with a specific role
        List<User> mockUsers = Arrays.asList(
                new User("john_doe","P@ssw0rd", "ROLE_user", "john", "doe", "Mr","john@example.com", null, true, "Am lange Felde", "Wien", 1220, "59/2/3","AT"),
                new User("jane_doe","P@ssw0rd", "ROLE_user", "jane", "doe", "Ms","jane@example.com", null, true, "Some Address", "City", 1000, "1/2/3", "AT")
        );
        when(userService.getUsersRole("ROLE_user")).thenReturn(mockUsers);

        // Call the getUsersRole method on the UserController
        List<User> result = userController.getUsersRole("ROLE_user");

        // Verify that the result matches the expected list of users
        assertEquals(mockUsers, result);
    }

    @Test
    void testGetUsersRole_NoUsersFound() {
        // Mock the behavior of the UserService to return an empty list (no users found with the role)
        when(userService.getUsersRole("nonexistentRole")).thenReturn(Collections.emptyList());

        // Call the getUsersRole method on the UserController
        List<User> result = userController.getUsersRole("nonexistentRole");

        // Verify that the result is an empty list for no users found
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testGetUsersFirstname_UsersExist() {
        // Mock the behavior of the UserService to return a list of users with a specific firstname
        List<User> mockUsers = Arrays.asList(
                new User("john_doe","P@ssw0rd", "ROLE_user", "john", "doe", "Mr","john@example.com", null, true, "Am lange Felde", "Wien", 1220, "59/2/3","AT"),
                new User("jane_doe","P@ssw0rd", "ROLE_user", "jane", "doe", "Ms","jane@example.com", null, true, "Some Address", "City", 1000, "1/2/3", "AT")
        );
        when(userService.getUsersFirstname("john")).thenReturn(mockUsers);

        // Call the getUsersFirstname method on the UserController
        List<User> result = userController.getUsersFirstname("john");

        // Verify that the result matches the expected list of users
        assertEquals(mockUsers, result);
    }

    @Test
    void testGetUsersFirstname_NoUsersFound() {
        // Mock the behavior of the UserService to return an empty list (no users found with the firstname)
        when(userService.getUsersFirstname("nonexistentName")).thenReturn(Collections.emptyList());

        // Call the getUsersFirstname method on the UserController
        List<User> result = userController.getUsersFirstname("nonexistentName");

        // Verify that the result is an empty list for no users found
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testGetUsersLastname_UsersExist() {
        // Mock the behavior of the UserService to return a list of users with a specific lastname
        List<User> mockUsers = Arrays.asList(
                new User("john_doe","P@ssw0rd", "ROLE_user", "john", "doe", "Mr","john@example.com", null, true, "Am lange Felde", "Wien", 1220, "59/2/3","AT"),
                new User("jane_doe","P@ssw0rd", "ROLE_user", "jane", "doe", "Ms","jane@example.com", null, true, "Some Address", "City", 1000, "1/2/3", "AT")
        );
        when(userService.getUsersLastname("doe")).thenReturn(mockUsers);

        // Call the getUsersLastname method on the UserController
        List<User> result = userController.getUsersLastname("doe");

        // Verify that the result matches the expected list of users
        assertEquals(mockUsers, result);
    }

    @Test
    void testGetUsersLastname_NoUsersFound() {
        // Mock the behavior of the UserService to return an empty list (no users found with the lastname)
        when(userService.getUsersLastname("nonexistentName")).thenReturn(Collections.emptyList());

        // Call the getUsersLastname method on the UserController
        List<User> result = userController.getUsersLastname("nonexistentName");

        // Verify that the result is an empty list for no users found
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testGetUserEmail_UserExists() {
        // Mock the behavior of the UserService to return a user with a specific email
        User mockUser = new User("john_doe","P@ssw0rd", "ROLE_user", "john", "doe", "Mr","john@example.com", null, true, "Am lange Felde", "Wien", 1220, "59/2/3","AT");
        when(userService.getUserEmail("john@example.com")).thenReturn(mockUser);

        // Call the getUserEmail method on the UserController
        User result = userController.getUserEmail("john@example.com");

        // Verify that the result matches the expected user
        assertEquals(mockUser, result);
    }

    @Test
    void testGetUserEmail_UserNotFound() {
        // Mock the behavior of the UserService to return null (user not found with the email)
        when(userService.getUserEmail("nonexistentEmail@example.com")).thenReturn(null);

        // Call the getUserEmail method on the UserController
        User result = userController.getUserEmail("nonexistentEmail@example.com");

        // Verify that the result is null for a user not found
        assertNull(result);
    }

    @Test
    void testGetUsersCountry_UsersExist() {
        // Mock the behavior of the UserService to return a list of users with a specific country
        List<User> mockUsers = Arrays.asList(
                new User("john_doe","P@ssw0rd", "ROLE_user", "john", "doe", "Mr","john@example.com", null, true, "Am lange Felde", "Wien", 1220, "59/2/3","AT"),
                new User("jane_doe","P@ssw0rd", "ROLE_user", "jane", "doe", "Ms","jane@example.com", null, true, "Some Address", "City", 1000, "1/2/3", "AT")
        );
        when(userService.getUsersCountry("AT")).thenReturn(mockUsers);

        // Call the getUsersCountry method on the UserController
        List<User> result = userController.getUsersCountry("AT");

        // Verify that the result matches the expected list of users
        assertEquals(mockUsers, result);
    }

    @Test
    void testGetUsersCountry_NoUsersFound() {
        // Mock the behavior of the UserService to return an empty list (no users found with the country)
        when(userService.getUsersCountry("nonexistentCountry")).thenReturn(Collections.emptyList());

        // Call the getUsersCountry method on the UserController
        List<User> result = userController.getUsersCountry("nonexistentCountry");

        // Verify that the result is an empty list for no users found
        assertEquals(Collections.emptyList(), result);
    }



    @Test
    void testUploadImage_EmptyFile() {
        // Create an empty mock MultipartFile
        MockMultipartFile file = new MockMultipartFile("image", "", "image/jpeg", new byte[0]);

        // Call the uploadImage method on the UserController
        ResponseEntity<?> result = userController.uploadImage("john_doe", file);

        // Verify that the result is a BAD_REQUEST response
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Please select a file to upload.", result.getBody());
    }








}

