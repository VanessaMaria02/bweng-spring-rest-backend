package at.technikum.springrestbackend.controller;

import at.technikum.springrestbackend.model.Brand;
import at.technikum.springrestbackend.model.User;
import at.technikum.springrestbackend.service.BrandService;
import at.technikum.springrestbackend.util.BrandValidator;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.UUID;


@RestController
@CrossOrigin(origins = "*")
public class BrandController {

    private final BrandService brandService;
    private final BrandValidator brandValidator;

    @Autowired
    public BrandController(BrandService brandService, BrandValidator brandValidator) {
        this.brandService = brandService;
        this.brandValidator = brandValidator;

    }

    @GetMapping("/brands")
    public List<Brand> getBrands() {
        return brandService.getBrands();
    }

    @GetMapping("/brand/{name}")
    public Brand getBrand(@PathVariable String name) {
        return brandService.getBrandByname(name);
    }

    @PostMapping("/addbrand")
    public ResponseEntity<Object> addBrand(@RequestBody @Valid Brand brand) {
        return handleBrandCreation(brand);
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

    private ResponseEntity<Object> handleBrandCreation(Brand brand) {
        List<String> validationErrors = brandValidator.validateBrand(brand);
        if (!validationErrors.isEmpty()) {
            return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
        }
        brandService.createBrand(brand);
        return new ResponseEntity<>("New brand is saved.", HttpStatus.CREATED);
    }
    private ResponseEntity<Object> handleBrandDeletion(Brand brandToDelete) {
        if (brandToDelete == null) {
            return new ResponseEntity<>("Brand not found", HttpStatus.NOT_FOUND);
        }
        brandService.deleteBrand(brandToDelete.getName());
        return new ResponseEntity<>("Brand deleted successfully", HttpStatus.OK);
    }
    private ResponseEntity<Object> handleBrandUpdate(String name, Brand updatedBrand) {
        int affectedRows = brandService.updateBrandInfo(name, updatedBrand.getName(), updatedBrand.getPicturePath());

        if (affectedRows > 0) {
            return new ResponseEntity<>("Brand info has been updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Brand not found", HttpStatus.NOT_FOUND);
        }
    }
}
