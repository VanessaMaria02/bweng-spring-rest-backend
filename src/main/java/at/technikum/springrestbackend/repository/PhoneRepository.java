package at.technikum.springrestbackend.repository;

import at.technikum.springrestbackend.model.Brand;
import at.technikum.springrestbackend.model.Phone;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PhoneRepository extends CrudRepository<Phone, UUID> {

    List<Phone> findByName(String name);

    List<Phone> findByDisplaySize(float displaySize);

    List<Phone> findByMemory(int memory);

    List<Phone> findByBattery(int battery);

    List<Phone> findByPrice(float price);

    List<Phone> findByBrand(Brand brand);

    @Transactional
    void deletePhoneById(UUID id);

    @Override
    List<Phone> findAll();
    @Modifying
    @Query("UPDATE Phone p SET p.name = :newName, p.description = :newDescription, p.displaySize = :newDisplaySize, p.memory = :newMemory, p.battery = :newBattery, p.price = :newPrice, p.picture = :newImage WHERE p.id = :oldId")
    int updatePhoneInfo(@Param("oldId") UUID oldId, @Param("newName") String newName, @Param("newDescription") String newDescription, @Param("newDisplaySize") float newDisplaySize, @Param("newMemory") int newMemory, @Param("newBattery") int newBattery, @Param("newPrice") float newPrice, @Param("newImage") String newImage);
}

