package at.technikum.springrestbackend.repository;

import at.technikum.springrestbackend.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {

   User findByUsername(String username);

   List<User> findByRole(int role);

   List<User> findByFirstname(String firstname);

   List<User> findByLastname(String lastname);

   User findByEmail(String mail);

   List<User> findByCountry(String country);

   List<User> findByStatus(boolean status);
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
    @Override
    List<User> findAll();
}
