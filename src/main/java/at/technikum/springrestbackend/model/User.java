package at.technikum.springrestbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.Instant;
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

    @NotBlank(message = "Salutation is required")
    private String salutation;

    @NotBlank(message = "E-mail is required")
    @Email(message = "Invalid email address")
    private String email;

    @NotBlank(message = "CountryCode is required")
    private String countryCode;

    private int postalCode;

    private String city;

    private String street;

    private String houseNumber;

    private String profilePicture;

    private boolean status;

    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant lastUpdatedOn;

    public User (String username, String password, String role, String firstname,
                 String lastname, String salutation, String email,
                 String profilePicture, boolean status, String street,  String city, int postalCode, String houseNumber, String countryCode){
        this.username = username;
        this.password = password;
        this.role = role;
        this.firstname = firstname;
        this.lastname = lastname;
        this.salutation = salutation;
        this.email = email;
        this.profilePicture = profilePicture;
        this.status = status;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.houseNumber = houseNumber;
        this.countryCode = countryCode;

    }


    public boolean getStatus() {
        return this.status;
    }
}
