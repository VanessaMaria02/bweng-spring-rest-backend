package at.technikum.springrestbackend.controller;

import at.technikum.springrestbackend.model.Brand;
import at.technikum.springrestbackend.model.User;
import at.technikum.springrestbackend.service.BrandService;
import at.technikum.springrestbackend.service.UserService;
import at.technikum.springrestbackend.util.BrandValidator;
import com.auth0.jwt.exceptions.TokenExpiredException;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@CrossOrigin(origins = "*")
public class BrandController {

    private final BrandService brandService;
    private final BrandValidator brandValidator;

    private final UserService userService;
    @Autowired
    public BrandController(BrandService brandService, BrandValidator brandValidator, UserService userService) {
        this.brandService = brandService;
        this.brandValidator = brandValidator;

        this.userService = userService;
    }

    @GetMapping("/brands")
    public List<Brand> getBrands() {

        return brandService.getBrands();
    }

    @GetMapping("/brand/{name}")
    public Brand getBrand(@PathVariable String name) {
        return brandService.getBrandByname(name);
    }
    @PostMapping("/addbrand/{username}")
    public ResponseEntity<Object> addBrand(@PathVariable @Valid String username,
                                           @RequestBody @Valid Brand brand) {
        return handleBrandCreation(brand, username);
    }


    @DeleteMapping("/deletebrand/{name}")
    public ResponseEntity<Object> deleteUser(@PathVariable String name) {
        Brand brandToDelete = brandService.getBrandByname(name);
        return handleBrandDeletion(brandToDelete);
    }

    @PutMapping("/updatebrand/{name}")
    public ResponseEntity<Object> updateBrand(@PathVariable String name, @RequestBody @Valid Brand updatedBrand) {
        return handleBrandUpdate(name, updatedBrand);
    }

    private ResponseEntity<Object> handleBrandCreation(Brand brand, String username) {
        User user = userService.getUserByUsername(username);

        List<String> validationErrors = brandValidator.validateBrand(brand);
        if (!validationErrors.isEmpty()) {
            return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
        }

        if (user == null) {
            return new ResponseEntity<>("No User with that username", HttpStatus.BAD_REQUEST);
        } else {
            // Retrieve the managed User entity from the database using the user's ID
            User managedUser = userService.getUser(user.getId());

            // Set the managed User entity to the Brand
            brand.setCreatedBy(managedUser);
        }

        try {
            brandService.createBrand(brand);
            return new ResponseEntity<>("New brand is saved.", HttpStatus.CREATED);
        }catch (TokenExpiredException e){
            return new ResponseEntity<>("The JWT Token is expired, pleas login in again", HttpStatus.UNAUTHORIZED);
        }catch (Exception e) {
            // Handle other exceptions
            return new ResponseEntity<>("An error occurred while processing your request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private ResponseEntity<Object> handleBrandDeletion(Brand brandToDelete) {
        if (brandToDelete == null) {
            return new ResponseEntity<>("Brand not found", HttpStatus.NOT_FOUND);
        }
        try{
            brandService.deleteBrand(brandToDelete.getName());
            return new ResponseEntity<>("Brand deleted successfully", HttpStatus.OK);

        }catch (DataIntegrityViolationException e) {
            // Handle foreign key constraint violation (associated phones present)
            return new ResponseEntity<>("Cannot delete the brand. It has associated phones.", HttpStatus.CONFLICT);
        } catch (TokenExpiredException e){
            return new ResponseEntity<>("The JWT Token is expired, pleas login in again", HttpStatus.UNAUTHORIZED);
        }catch (Exception e) {
            // Handle other exceptions
            return new ResponseEntity<>("An error occurred while processing your request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    private ResponseEntity<Object> handleBrandUpdate(String name, Brand updatedBrand) {
        int affectedRows = 0;

        try {
            affectedRows = brandService.updateBrandInfo(name, updatedBrand.getName(), updatedBrand.getPicturePath());
        }catch (TokenExpiredException e){
            return new ResponseEntity<>("The JWT Token is expired, pleas login in again", HttpStatus.UNAUTHORIZED);
        }catch (Exception e) {
            // Handle other exceptions
            return new ResponseEntity<>("An error occurred while processing your request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }


        if (affectedRows > 0) {
            return new ResponseEntity<>("Brand info has been updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Brand not found", HttpStatus.NOT_FOUND);
        }
    }

}