package at.technikum.springrestbackend.util;

import at.technikum.springrestbackend.model.Brand;
import at.technikum.springrestbackend.service.BrandService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BrandValidator {

    private final BrandService brandService;

    public BrandValidator(BrandService brandService) {
        this.brandService = brandService;
    }

    public List<String> validateBrand(Brand brand) {
        List<String> validationErrors = new ArrayList<>();

        if (brandService.isBrandTaken(brand.getName())) {
            validationErrors.add("Brand is already exists");
        }
        return validationErrors;
    }
}
