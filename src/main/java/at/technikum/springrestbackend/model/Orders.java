package at.technikum.springrestbackend.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Orders {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

   //does not need not blank because will be set manually
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private  User user;

   //does not need not blank because will be set manually
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<Phone> phones = new ArrayList<>();

    private Timestamp timestamp;

    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant lastUpdatedOn;


    public Orders(User user){
        this.user = user;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }
    public Orders(UUID id, User user, List<Phone> phones) {
        this.id = id;
        this.user = user;
        this.phones = phones;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public void addPhone(Phone phone){phones.add(phone);}
}
