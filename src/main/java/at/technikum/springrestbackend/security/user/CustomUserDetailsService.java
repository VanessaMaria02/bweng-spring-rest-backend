package at.technikum.springrestbackend.security.user;

import at.technikum.springrestbackend.model.User;
import at.technikum.springrestbackend.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            User user = userRepository.findByUsername(username);

            return new UserPrincipal(user.getId(), user.getUsername(), user.getPassword(), user.getRole());
        }catch (EntityNotFoundException e){
            throw new UsernameNotFoundException(username);
        }
    }
}
