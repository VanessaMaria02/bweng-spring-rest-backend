package at.technikum.springrestbackend.controller;

import at.technikum.springrestbackend.model.User;
import at.technikum.springrestbackend.security.JwtToPrincipalConverter;
import at.technikum.springrestbackend.service.UserService;
import at.technikum.springrestbackend.util.UserValidator;
import com.auth0.jwt.exceptions.TokenExpiredException;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;
    private final UserValidator userValidator;

    public UserController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/users/userid/{id}")
    public User getUser(@PathVariable UUID id) {
        return userService.getUser(id);
    }

    @GetMapping("user/role/{username}")
    public String getUserRole(@PathVariable String username){
        User user = userService.getUserByUsername(username);

        if(user != null){
            return user.getRole();
        }else{
            return "No user with this name found";
        }


    }

    @GetMapping("/users/username/{username}")
    public User getUserUserName(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/users/role/{role}")
    public List<User> getUsersRole(@PathVariable String role) {
        return userService.getUsersRole(role);
    }

    @GetMapping("/users/firstname/{firstname}")
    public List<User> getUsersFirstname(@PathVariable String firstname) {
        return userService.getUsersFirstname(firstname);
    }

    @GetMapping("/users/lastname/{lastname}")
    public List<User> getUsersLastname(@PathVariable String lastname) {
        return userService.getUsersLastname(lastname);
    }

    @GetMapping("/users/email/{email}")
    public User getUserEmail(@PathVariable String email) {
        return userService.getUserEmail(email);
    }

    @GetMapping("/users/country/{country}")
    public List<User> getUsersCountry(@PathVariable String country) {
        return userService.getUsersCountry(country);
    }



    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody @Valid User user) {
        return handleUserCreation(user);
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable UUID id) {
        try{
            User userToDelete = userService.getUser(id);
            return handleUserDeletion(userToDelete);
        } catch (TokenExpiredException e){
            return new ResponseEntity<>("The JWT Token is expired, pleas login in again", HttpStatus.UNAUTHORIZED);
        }catch (Exception e) {
            // Handle other exceptions
            return new ResponseEntity<>("An error occurred while processing your request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/updateUser/{name}")
    public ResponseEntity<Object> updateUser(@PathVariable String name, @RequestBody @Valid User updatedUser) {
        return handleUserUpdate(name, updatedUser);
    }

    private ResponseEntity<Object> handleUserCreation(User user) {
        List<String> validationErrors = userValidator.validateUserRegistration(user);
        if (!validationErrors.isEmpty()) {
            return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        try {
            userService.createUser(user);
            return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
        }catch (TokenExpiredException e){
            return new ResponseEntity<>("The JWT Token is expired, pleas login in again", HttpStatus.UNAUTHORIZED);
        }catch (Exception e) {
            // Handle other exceptions
            return new ResponseEntity<>("An error occurred while processing your request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Object> handleUserDeletion(User userToDelete) {
        if (userToDelete == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        userService.deleteUser(userToDelete.getId());
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

    private ResponseEntity<Object> handleUserUpdate(String name, User updatedUser) {
        String username = JwtToPrincipalConverter.getCurrentUsername();
        String userRole = JwtToPrincipalConverter.getCurrentUserRole();


        if(!Objects.equals(username, name) && !userRole.equals("ROLE_admin")){
            return new ResponseEntity<>("Users can only edit there own Profile (except admins)", HttpStatus.UNAUTHORIZED);
        }


            int affectedRows = 0;

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));

        try{
            affectedRows = userService.updateUserInfo(
                    name,
                    updatedUser.getUsername(),
                    updatedUser.getPassword(),
                    updatedUser.getRole(),
                    updatedUser.getFirstname(),
                    updatedUser.getLastname(),
                    updatedUser.getSalutation(),
                    updatedUser.getEmail(),
                    updatedUser.getCountryCode(),
                    updatedUser.getPostalCode(),
                    updatedUser.getStreet(),
                    updatedUser.getCity(),
                    updatedUser.getHouseNumber(),
                    updatedUser.getProfilePicture(),
                    updatedUser.getStatus()
            );
        }catch (TokenExpiredException e){
            return new ResponseEntity<>("The JWT Token is expired, pleas login in again", HttpStatus.UNAUTHORIZED);
        }catch (Exception e) {
            // Handle other exceptions
            return new ResponseEntity<>("An error occurred while processing your request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }


        if (affectedRows > 0) {
            return new ResponseEntity<>("User info has been updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }
}
