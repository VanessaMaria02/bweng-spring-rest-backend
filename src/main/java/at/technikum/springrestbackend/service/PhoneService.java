package at.technikum.springrestbackend.service;

import at.technikum.springrestbackend.model.Brand;
import at.technikum.springrestbackend.model.Phone;
import at.technikum.springrestbackend.repository.PhoneRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PhoneService {

    private final PhoneRepository phoneRepository;

    public List<Phone> getPhones() {
        return phoneRepository.findAll();
    }

    public Phone getPhone(UUID id) {
        return phoneRepository.findById(id).orElseThrow();
    }

    public List<Phone> getPhonesName(String name) {
        return phoneRepository.findByName(name);
    }

    public List<Phone> getPhonesDisplay(float displaySize) {
        return phoneRepository.findByDisplaySize(displaySize);
    }

    public List<Phone> getPhonesMemory(int memory) {
        return phoneRepository.findByMemory(memory);
    }

    public List<Phone> getPhonesBattery(int battery) {
        return phoneRepository.findByBattery(battery);
    }

    public List<Phone> getPhonesPrice(float price) {
        return phoneRepository.findByPrice(price);
    }

    public List<Phone> getPhonesBrand(Brand brand) {
        return phoneRepository.findByBrand(brand);
    }

    public Phone createPhone(Phone phone) {
        return phoneRepository.save(phone);
    }
    public void deletePhone(UUID id) {
        phoneRepository.deletePhoneById(id);
    }
    @Transactional
    public int updatePhoneInfo(UUID oldId, String newName, String newDescription, float newDisplaySize, int newMemory, int newBattery, float newPrice) {
        return phoneRepository.updatePhoneInfo(oldId, newName, newDescription, newDisplaySize, newMemory, newBattery, newPrice);
    }
}

