package at.technikum.springrestbackend.service;


import at.technikum.springrestbackend.model.User;
import at.technikum.springrestbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(UUID id) {
        return userRepository.findById(id).orElseThrow();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getUsersRole(String role) {
        return userRepository.findByRole(role);
    }

    public List<User> getUsersFirstname(String firstname) {
        return userRepository.findByFirstname(firstname);
    }

    public List<User> getUsersLastname(String lastname) {
        return userRepository.findByLastname(lastname);
    }

    public User getUserEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getUsersCountry(String country) {
        return userRepository.findByCountryCode(country);
    }

    public List<User> getUsersStatus(boolean status) {
        return userRepository.findByStatus(status);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public void deleteUser(UUID id) {
        userRepository.deleteUserById(id);
    }
    @Transactional
    public int updateUserInfo(String oldUsername, String newUsername,String newPassword, String newRole,String newFirstname, String newLastname, String newSalutation, String newEmail, String newCountryCode, int newPostalCode, String newStreet, String newCity, String newHouseNumber,  String newProfilePicture, boolean newStatus) {
        return userRepository.updateUserInfo(oldUsername, newUsername,newPassword,newRole,newFirstname,newLastname,newSalutation, newEmail,newCountryCode, newPostalCode, newStreet, newCity, newHouseNumber, newProfilePicture, newStatus);
    }
    @Transactional
    public void updateUserStatus(UUID userId, boolean newStatus) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setStatus(newStatus);
        userRepository.save(user);
    }

}
