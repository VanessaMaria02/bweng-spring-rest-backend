package at.technikum.springrestbackend.service;

import at.technikum.springrestbackend.model.Brand;
import at.technikum.springrestbackend.repository.BrandRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    public List<Brand> getBrands(){
        return brandRepository.findAll();
    }
    public Brand getBrandByname(String name){
        return brandRepository.findByname(name);
    }
    public Brand createBrand(Brand brand){
        return brandRepository.save(brand);
    }
    public boolean isBrandTaken(String name) {
        return brandRepository.existsByname(name);
    }
    @Transactional
    public void deleteBrand(String name){
        brandRepository.deleteBrandByname(name);
    }
    public Brand updateBrand(Brand brand){ return brandRepository.save(brand);}
    @Transactional
    public int updateBrandInfo(String oldName, String newName, String newPicturePath) {
        return brandRepository.updateBrandInfo(oldName,newName, newPicturePath);
    }

}
