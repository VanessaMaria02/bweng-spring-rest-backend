package at.technikum.springrestbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;




import java.util.UUID;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "User", uniqueConstraints = {
       @UniqueConstraint(columnNames = {"username"}),
        @UniqueConstraint(columnNames = {"email"})
})
public class User {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Role is required")
    private String role;

    @NotBlank(message = "First name is required")
    private String firstname;

    @NotBlank(message = "Last name is required")
    private String lastname;

    @Enumerated(EnumType.STRING)
    private Salutation salutation;

    @Email(message = "Invalid email address")
    private String email;

    @NotBlank(message = "Country is required")
    private String country;

    private String profilePicture;

    private boolean status;

    public User (String username, String password, String role, String firstname,
                 String lastname, Salutation salutation, String email,
                 String country, String profilePicture, boolean status){
        this.username = username;
        this.password = password;
        this.role = role;
        this.firstname = firstname;
        this.lastname = lastname;
        this.salutation = salutation;
        this.email = email;
        this.country = country;
        this.profilePicture = profilePicture;
        this.status = status;
    }



}
