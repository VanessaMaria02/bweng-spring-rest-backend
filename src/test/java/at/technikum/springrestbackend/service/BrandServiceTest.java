package at.technikum.springrestbackend.service;

import at.technikum.springrestbackend.model.Brand;
import at.technikum.springrestbackend.repository.BrandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandService brandService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getBrandShouldReturnBrand() {
        UUID id = UUID.randomUUID();
        Brand brand = new Brand();


        when(brandRepository.findById(id)).thenReturn(Optional.of(brand));

        Brand found = brandService.getBrand(id);

        assertNotNull(found);
        assertEquals(brand, found);
    }

    @Test
    public void getBrandsShouldReturnAllBrands() {
        List<Brand> brands = Arrays.asList(new Brand(), new Brand());

        when(brandRepository.findAll()).thenReturn(brands);

        List<Brand> found = brandService.getBrands();

        assertNotNull(found);
        assertEquals(2, found.size());
    }

    @Test
    public void createBrandShouldPersistBrand() {
        Brand brand = new Brand();

        when(brandRepository.save(brand)).thenReturn(brand);

        Brand created = brandService.createBrand(brand);

        assertNotNull(created);
    }
    @Test
    public void getBrandBynameShouldReturnBrand() {
        String name = "TestBrand";
        Brand brand = new Brand(name, "TestPath");

        when(brandRepository.findByname(name)).thenReturn(brand);

        Brand found = brandService.getBrandByname(name);

        assertNotNull(found);
        assertEquals(name, found.getName());
    }
    @Test
    public void isBrandTakenShouldReturnTrueWhenBrandExists() {
        String name = "ExistingBrand";
        when(brandRepository.existsByname(name)).thenReturn(true);

        boolean isTaken = brandService.isBrandTaken(name);

        assertTrue(isTaken);
    }

    @Test
    public void isBrandTakenShouldReturnFalseWhenBrandNotExists() {
        String name = "NonExistingBrand";
        when(brandRepository.existsByname(name)).thenReturn(false);

        boolean isTaken = brandService.isBrandTaken(name);

        assertFalse(isTaken);
    }
    @Test
    public void deleteBrandShouldInvokeRepositoryDeleteMethod() {
        String name = "BrandToDelete";
        doNothing().when(brandRepository).deleteBrandByname(name);

        brandService.deleteBrand(name);

        verify(brandRepository, times(1)).deleteBrandByname(name);
    }
    @Test
    public void updateBrandInfoShouldUpdateBrandDetails() {
        String oldName = "OldBrandName";
        String newName = "NewBrandName";
        String newPicturePath = "NewPath";
        when(brandRepository.updateBrandInfo(oldName, newName, newPicturePath)).thenReturn(1);

        int updateCount = brandService.updateBrandInfo(oldName, newName, newPicturePath);

        assertEquals(1, updateCount);
    }
}
