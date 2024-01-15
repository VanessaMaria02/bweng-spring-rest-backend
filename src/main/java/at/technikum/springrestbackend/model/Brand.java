package at.technikum.springrestbackend.model;


import jakarta.persistence.*;
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
@Table(name = "Brand", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
})
public class Brand {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Name of the brand is required")
    private String name;


    private String picturePath;

    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant lastUpdatedOn;

    @NotBlank(message = "User required")
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private User createdBy;

    public Brand(String name, String picturePath, User user) {
        this.name = name;
        this.picturePath = picturePath;
        this.createdBy = user;
    }

}


