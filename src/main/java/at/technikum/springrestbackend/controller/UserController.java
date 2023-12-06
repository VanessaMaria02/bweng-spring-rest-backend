package at.technikum.springrestbackend.controller;

import at.technikum.springrestbackend.dto.TokenRequest;
import at.technikum.springrestbackend.dto.TokenResponse;
import at.technikum.springrestbackend.model.User;
import at.technikum.springrestbackend.service.UserService;
import at.technikum.springrestbackend.util.PasswordValidator;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.UUID;

@RestController
public class UserController {
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/users/userid/{id}")
    public User getUser(UUID id) {
        return userService.getUser(id);
    }

    @GetMapping("/users/username/{username}")
    public User getUserUserName(String username) {
        return userService.getUserUsername(username);
    }

    @GetMapping("/users/role/{role}")
    public List<User> getUsersRole(int role) {
        return userService.getUsersRole(role);
    }

    @GetMapping("/users/firstname/{firstname}")
    public List<User> getUsersFirstname(String firstname) {
        return userService.getUsersFirstname(firstname);
    }

    @GetMapping("/users/lastname/{lastname}")
    public List<User> getUsersLastname(String lastname) {
        return userService.getUsersLastname(lastname);
    }

    @GetMapping("/users/email/{email}")
    public User getUserEmail(String email) {
        return userService.getUserEmail(email);
    }

    @GetMapping("/users/country/{country}")
    public List<User> getUsersCountry(String country) {
        return userService.getUsersCountry(country);
    }

    @GetMapping("/users/status/{status}")
    public List<User> getUsersStatus(boolean status) {
        return userService.getUsersStatus(status);
    }
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (!isValidRegistration(user)) {
            return new ResponseEntity<>("Invalid registration data", HttpStatus.BAD_REQUEST);
        }
        userService.createUser(user);

        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    private boolean isValidRegistration(User user) {

        if (userService.isUsernameTaken(user.getUsername())) {
            return false;
        }
        if (userService.isEmailTaken(user.getEmail())) {
            return false;
        }
        if (!PasswordValidator.isValidPassword(user.getPassword())) {
            return false;
        }
        return true;
    }
}


