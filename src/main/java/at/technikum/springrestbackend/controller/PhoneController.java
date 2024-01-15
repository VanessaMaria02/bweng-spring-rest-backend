package at.technikum.springrestbackend.controller;

import at.technikum.springrestbackend.model.Brand;
import at.technikum.springrestbackend.model.Phone;
import at.technikum.springrestbackend.model.User;
import at.technikum.springrestbackend.service.PhoneService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
public class PhoneController {
    private final PhoneService phoneService;

    public PhoneController(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @GetMapping("/phones")
    public List<Phone> getPhones() {
        return phoneService.getPhones();
    }

    @GetMapping("/phone/{id}")
    public Phone getPhone(@PathVariable UUID id) {
        return phoneService.getPhone(id);
    }

    @GetMapping("/phones/{name}")
    public List<Phone> getPhonesName(@PathVariable String name){
        return phoneService.getPhonesName(name);
    }
    @GetMapping("/phones/{display}")
    public List<Phone> getPhoneDisplay(@PathVariable float displaysize){
        return phoneService.getPhonesDisplay(displaysize);
    }
    @GetMapping("/phones/{memory}")
    public List<Phone> getPhonesMemory(@PathVariable int memory){
        return phoneService.getPhonesMemory(memory);
    }
    @GetMapping("/phones/{battery}")
    public List<Phone> getPhonesBattery(@PathVariable int battery){
        return phoneService.getPhonesBattery(battery);
    }
    @GetMapping("/phones/{price}")
    public List<Phone> getPhonePrice(@PathVariable float price){
        return phoneService.getPhonesPrice(price);
    }
    @GetMapping("/phones/{brand}")
    public List<Phone> getPhonesBrand(@PathVariable Brand brand){
        return phoneService.getPhonesBrand(brand);
    }
    @PostMapping("/addPhone")
    public Phone createPhone(@RequestBody Phone phone) {
        return phoneService.createPhone(phone);
    }
    @DeleteMapping("/deletePhone/{id}")
    public ResponseEntity<Object> deletePhone(@PathVariable UUID id) {
        Phone phoneToDelete = phoneService.getPhone(id);
        return handlePhoneDeletion(phoneToDelete);
    }
    @PutMapping("/updatePhone/{id}")
    public ResponseEntity<Object> updatePhone(@PathVariable UUID id, @RequestBody @Valid Phone updatedPhone) {
        return handlePhoneUpdate(id, updatedPhone);
    }
    private ResponseEntity<Object> handlePhoneDeletion(Phone phoneToDelete) {
        if (phoneToDelete == null) {
            return new ResponseEntity<>("Phone not found", HttpStatus.NOT_FOUND);
        }

        phoneService.deletePhone(phoneToDelete.getId());
        return new ResponseEntity<>("Phone deleted successfully", HttpStatus.OK);
    }

    private ResponseEntity<Object> handlePhoneUpdate(UUID id, Phone updatedPhone) {
        int affectedRows = phoneService.updatePhoneInfo(id, updatedPhone.getName(), updatedPhone.getDescription(),updatedPhone.getDisplaySize(),updatedPhone.getMemory(),updatedPhone.getBattery(),updatedPhone.getPrice());

        if (affectedRows > 0) {
            return new ResponseEntity<>("Phone info has been updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Phone not found", HttpStatus.NOT_FOUND);
        }
    }
}

