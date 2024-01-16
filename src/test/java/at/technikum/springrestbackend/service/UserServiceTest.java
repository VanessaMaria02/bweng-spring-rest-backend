package at.technikum.springrestbackend.service;

import at.technikum.springrestbackend.model.User;
import at.technikum.springrestbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getUsersShouldReturnAllUsers() {
        List<User> usersList = Arrays.asList(new User(), new User());

        when(userRepository.findAll()).thenReturn(usersList);

        List<User> foundUsers = userService.getUsers();

        assertNotNull(foundUsers);
        assertEquals(2, foundUsers.size());
    }

    @Test
    public void getUserShouldReturnUserWhenIdExists() {
        UUID id = UUID.randomUUID();
        User user = new User();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User found = userService.getUser(id);

        assertNotNull(found);
    }

    @Test
    public void getUserShouldThrowWhenIdDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            userService.getUser(id);
        });
    }

    @Test
    public void getUserByUsernameShouldReturnUserWhenUsernameExists() {
        String username = "sampleUser";
        User user = new User();

        when(userRepository.findByUsername(username)).thenReturn(user);

        User found = userService.getUserByUsername(username);

        assertNotNull(found);
    }

    @Test
    public void getUsersRoleShouldReturnUsersWithGivenRole() {
        String role = "user";
        List<User> usersList = Arrays.asList(new User(), new User());

        when(userRepository.findByRole(role)).thenReturn(usersList);

        List<User> foundUsers = userService.getUsersRole(role);

        assertNotNull(foundUsers);
        assertEquals(2, foundUsers.size());
    }

    @Test
    public void getUsersFirstnameShouldReturnUsersWithGivenFirstname() {
        String firstname = "John";
        List<User> usersList = Arrays.asList(new User(), new User());

        when(userRepository.findByFirstname(firstname)).thenReturn(usersList);

        List<User> foundUsers = userService.getUsersFirstname(firstname);

        assertNotNull(foundUsers);
        assertEquals(2, foundUsers.size());
    }

    @Test
    public void getUsersLastnameShouldReturnUsersWithGivenLastname() {
        String lastname = "Doe";
        List<User> usersList = Arrays.asList(new User(), new User());

        when(userRepository.findByLastname(lastname)).thenReturn(usersList);

        List<User> foundUsers = userService.getUsersLastname(lastname);

        assertNotNull(foundUsers);
        assertEquals(2, foundUsers.size());
    }

    @Test
    public void getUserEmailShouldReturnUserWhenEmailExists() {
        String email = "sample@example.com";
        User user = new User();

        when(userRepository.findByEmail(email)).thenReturn(user);

        User found = userService.getUserEmail(email);

        assertNotNull(found);
    }

    @Test
    public void getUsersCountryShouldReturnUsersWithGivenCountry() {
        String country = "US";
        List<User> usersList = Arrays.asList(new User(), new User());

        when(userRepository.findByCountryCode(country)).thenReturn(usersList);

        List<User> foundUsers = userService.getUsersCountry(country);

        assertNotNull(foundUsers);
        assertEquals(2, foundUsers.size());
    }

    @Test
    public void getUsersStatusShouldReturnUsersWithGivenStatus() {
        boolean status = true;
        List<User> usersList = Arrays.asList(new User(), new User());

        when(userRepository.findByStatus(status)).thenReturn(usersList);

        List<User> foundUsers = userService.getUsersStatus(status);

        assertNotNull(foundUsers);
        assertEquals(2, foundUsers.size());
    }

    @Test
    public void createUserShouldPersistUser() {
        User user = new User();

        when(userRepository.save(user)).thenReturn(user);
        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
    }

    @Test
    public void isUsernameTakenShouldReturnTrueWhenUsernameExists() {
        String username = "sampleUser";

        when(userRepository.existsByUsername(username)).thenReturn(true);

        boolean isTaken = userService.isUsernameTaken(username);

        assertTrue(isTaken);
    }

    @Test
    public void isUsernameTakenShouldReturnFalseWhenUsernameDoesNotExist() {
        String username = "sampleUser";

        when(userRepository.existsByUsername(username)).thenReturn(false);

        boolean isTaken = userService.isUsernameTaken(username);

        assertFalse(isTaken);
    }

    @Test
    public void isEmailTakenShouldReturnTrueWhenEmailExists() {
        String email = "sample@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        boolean isTaken = userService.isEmailTaken(email);

        assertTrue(isTaken);
    }

    @Test
    public void isEmailTakenShouldReturnFalseWhenEmailDoesNotExist() {
        String email = "sample@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(false);

        boolean isTaken = userService.isEmailTaken(email);

        assertFalse(isTaken);
    }

    @Test
    public void deleteUserShouldInvokeRepositoryDeleteMethod() {
        UUID id = UUID.randomUUID();
        doNothing().when(userRepository).deleteUserById(id);

        userService.deleteUser(id);

        verify(userRepository, times(1)).deleteUserById(id);
    }

    @Test
    public void updateUserInfoShouldUpdateUserDetails() {
        String oldUsername = "oldUser";
        String newUsername = "newUser";
        String newPassword = "newPassword";
        String newRole = "admin";
        String newFirstname = "John";
        String newLastname = "Doe";
        String newSalutation = "Mr.";
        String newEmail = "new@example.com";
        String newCountryCode = "US";
        int newPostalCode = 12345;
        String newStreet = "New Street";
        String newCity = "New City";
        String newHouseNumber = "123";
        String newProfilePicture = "new_profile.jpg";
        boolean newStatus = true;

        when(userRepository.updateUserInfo(
                oldUsername, newUsername, newPassword, newRole, newFirstname, newLastname, newSalutation, newEmail,
                newCountryCode, newPostalCode, newStreet, newCity, newHouseNumber, newProfilePicture, newStatus))
                .thenReturn(1);

        int updateCount = userService.updateUserInfo(
                oldUsername, newUsername, newPassword, newRole, newFirstname, newLastname, newSalutation, newEmail,
                newCountryCode, newPostalCode, newStreet, newCity, newHouseNumber, newProfilePicture, newStatus);

        assertEquals(1, updateCount);
    }
}
