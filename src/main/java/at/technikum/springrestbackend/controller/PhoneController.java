package at.technikum.springrestbackend.controller;

import at.technikum.springrestbackend.model.Brand;
import at.technikum.springrestbackend.model.Phone;
import at.technikum.springrestbackend.model.User;
import at.technikum.springrestbackend.service.BrandService;
import at.technikum.springrestbackend.service.PhoneService;
import at.technikum.springrestbackend.service.UserService;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
public class PhoneController {
    private final PhoneService phoneService;

    private final UserService userService;

    private final BrandService brandService;

    public PhoneController(PhoneService phoneService, UserService userService, BrandService brandService) {
        this.phoneService = phoneService;
        this.userService = userService;
        this.brandService = brandService;
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
    @PostMapping("/addPhone/{username}/{brand}")
    public ResponseEntity<Object> createPhone(@PathVariable @Valid String username,
                                              @PathVariable @Valid String brand,
                                              @RequestBody @Valid Phone phone) {
        return handlePhoneCreation(username, brand, phone);
    }

    @PostMapping("/phones/upload/{id}")
    public ResponseEntity<?> uploadImage(@PathVariable UUID id, @RequestParam("image") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return new ResponseEntity<>("Please select a file to upload.", HttpStatus.BAD_REQUEST);
        }

        try {
            String directoryPath = "../Frontend/src/pics";

            Path directory = Paths.get(directoryPath);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }



            String fileName = file.getOriginalFilename();
            Path filePath = directory.resolve(fileName);

            Files.copy(file.getInputStream(), filePath);
            Phone phone = phoneService.getPhone(id) ;
            if(phone == null ){
                return new ResponseEntity<>("Phone not found", HttpStatus.NOT_FOUND);
            }
            phone.setPicture(filePath.toString());
            return handlePhoneUpdate(id, phone);



        } catch (IOException ex) {
            return new ResponseEntity<>("An error occurred while uploading your file.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Object> handlePhoneCreation(String username, String brand, Phone phone) {
    User user = userService.getUserByUsername(username);
    Brand newBrand = brandService.getBrandByname(brand);

    if(user == null){
        return new ResponseEntity<>("No User with that username", HttpStatus.BAD_REQUEST);
    }else if(newBrand == null){
        return new ResponseEntity<>("No Brand with that name", HttpStatus.BAD_REQUEST);
    }else {
        User mangedUser = userService.getUser(user.getId());
        Brand mangedBrand = brandService.getBrand(newBrand.getId());

        phone.setCreatedBy(mangedUser);
        phone.setBrand(mangedBrand);
    }

    try{
        phoneService.createPhone(phone);
        return new ResponseEntity<>("New phone is saved.", HttpStatus.CREATED);
    }catch (TokenExpiredException e){
        return new ResponseEntity<>("The JWT Token is expired, pleas login in again", HttpStatus.UNAUTHORIZED);
    }catch (Exception e) {
        // Handle other exceptions
        return new ResponseEntity<>("An error occurred while processing your request.", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    }

    @DeleteMapping("/deletePhone/{id}")
    public ResponseEntity<Object> deletePhone(@PathVariable UUID id) {

        try{
            Phone phoneToDelete = phoneService.getPhone(id);
        return handlePhoneDeletion(phoneToDelete);
        }catch (DataIntegrityViolationException e) {
            // Handle foreign key constraint violation
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Cannot delete the phone. It has associated orders.");
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing your request.");
        }
    }
    @PutMapping("/updatePhone/{id}")
    public ResponseEntity<Object> updatePhone(@PathVariable UUID id, @RequestBody @Valid Phone updatedPhone) {
        return handlePhoneUpdate(id, updatedPhone);
    }
    private ResponseEntity<Object> handlePhoneDeletion(Phone phoneToDelete) {
        if (phoneToDelete == null) {
            return new ResponseEntity<>("Phone not found", HttpStatus.NOT_FOUND);
        }

        try{
            phoneService.deletePhone(phoneToDelete.getId());
            return new ResponseEntity<>("Phone deleted successfully", HttpStatus.OK);
        }catch (DataIntegrityViolationException e) {
            // Handle foreign key constraint violation
            return new ResponseEntity<>("Cannot delete the phone. It has associated orders.", HttpStatus.CONFLICT);
        } catch (TokenExpiredException e){
            return new ResponseEntity<>("The JWT Token is expired, pleas login in again", HttpStatus.UNAUTHORIZED);
        }catch (Exception e) {
            // Handle other exceptions
            return new ResponseEntity<>("An error occurred while processing your request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private ResponseEntity<Object> handlePhoneUpdate(UUID id, Phone updatedPhone) {
       int affectedRows = 0;

       try {
           affectedRows = phoneService.updatePhoneInfo(id, updatedPhone.getName(), updatedPhone.getDescription(),updatedPhone.getDisplaySize(),updatedPhone.getMemory(),updatedPhone.getBattery(),updatedPhone.getPrice());

       }catch (TokenExpiredException e){
           return new ResponseEntity<>("The JWT Token is expired, pleas login in again", HttpStatus.UNAUTHORIZED);
       }catch (Exception e) {
           // Handle other exceptions
           return new ResponseEntity<>("An error occurred while processing your request.", HttpStatus.INTERNAL_SERVER_ERROR);
       }

        if (affectedRows > 0) {
            return new ResponseEntity<>("Phone info has been updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Phone not found", HttpStatus.NOT_FOUND);
        }
    }
}

