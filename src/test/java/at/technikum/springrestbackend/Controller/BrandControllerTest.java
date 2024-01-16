package at.technikum.springrestbackend.Controller;

import at.technikum.springrestbackend.controller.BrandController;
import at.technikum.springrestbackend.model.Brand;
import at.technikum.springrestbackend.model.User;
import at.technikum.springrestbackend.service.BrandService;
import at.technikum.springrestbackend.service.UserService;
import at.technikum.springrestbackend.util.BrandValidator;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BrandControllerTest {

    @Mock
    private BrandService brandService;

    @Mock
    private BrandValidator brandValidator;

    @Mock
    private UserService userService;

    @InjectMocks
    private BrandController brandController;

    @Test
    void testGetBrands() {
        List<Brand> mockBrands = Arrays.asList(new Brand("Brand1", "Picture1"), new Brand("Brand2", "Picture2"));
        when(brandService.getBrands()).thenReturn(mockBrands);

        List<Brand> result = brandController.getBrands();

        assertEquals(mockBrands, result);
    }

    @Test
    void testGetBrand_BrandExists() {
        Brand mockBrand = new Brand("Brand1", "Picture1");
        when(brandService.getBrandByname("Brand1")).thenReturn(mockBrand);

        Brand result = brandController.getBrand("Brand1");

        assertEquals(mockBrand, result);
    }

    @Test
    void testGetBrand_BrandNotFound() {
        when(brandService.getBrandByname("NonexistentBrand")).thenReturn(null);

        Brand result = brandController.getBrand("NonexistentBrand");

        assertEquals(null, result);
    }

    @Test
    void testAddBrand_ValidBrand    () {
        when(brandValidator.validateBrand(any(Brand.class))).thenReturn(Collections.emptyList());
        when(userService.getUserByUsername(anyString())).thenReturn(new User());

        ResponseEntity<Object> result = brandController.addBrand("john_doe", new Brand("Brand1", "Picture1"));

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("New brand is saved.", result.getBody());
    }

    @Test
    void testAddBrand_InvalidBrand() {
        when(brandValidator.validateBrand(any(Brand.class))).thenReturn(Arrays.asList("Error 1", "Error 2"));
        when(userService.getUserByUsername(anyString())).thenReturn(new User());

        ResponseEntity<Object> result = brandController.addBrand("john_doe", new Brand("InvalidBrand", ""));

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(Arrays.asList("Error 1", "Error 2"), result.getBody());
    }

    @Test
    void testDeleteBrand_BrandNotFound() {
        when(brandService.getBrandByname("NonexistentBrand")).thenReturn(null);

        ResponseEntity<Object> result = brandController.deleteUser("NonexistentBrand");

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
    @Test
    void testAddBrand_UserNotFound() {
        when(userService.getUserByUsername("nonexistent_user")).thenReturn(null);

        ResponseEntity<Object> result = brandController.addBrand("nonexistent_user", new Brand("BrandX", "PictureX"));

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("No User with that username", result.getBody());
    }

    @Test
    void testAddBrand_TokenExpired() {
        when(userService.getUserByUsername(anyString())).thenReturn(new User());
        when(brandValidator.validateBrand(any(Brand.class))).thenReturn(Collections.emptyList());
        doThrow(new TokenExpiredException("Token expired", Instant.now())).when(brandService).createBrand(any(Brand.class));

        ResponseEntity<Object> result = brandController.addBrand("user", new Brand("BrandX", "PictureX"));

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("The JWT Token is expired, pleas login in again", result.getBody());
    }
    @Test
    void testDeleteBrand_Successful() {
        Brand brandToDelete = new Brand("ExistingBrand", "Picture");
        when(brandService.getBrandByname("ExistingBrand")).thenReturn(brandToDelete);

        ResponseEntity<Object> result = brandController.deleteUser("ExistingBrand");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Brand deleted successfully", result.getBody());
    }

    @Test
    void testDeleteBrand_Conflict() {
        Brand brandToDelete = new Brand("BrandWithDependencies", "Picture");
        when(brandService.getBrandByname("BrandWithDependencies")).thenReturn(brandToDelete);
        doThrow(new DataIntegrityViolationException("Cannot delete")).when(brandService).deleteBrand("BrandWithDependencies");

        ResponseEntity<Object> result = brandController.deleteUser("BrandWithDependencies");

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals("Cannot delete the brand. It has associated phones.", result.getBody());
    }
    @Test
    void testUpdateBrand_Successful() {
        when(brandService.updateBrandInfo(anyString(), anyString(), anyString())).thenReturn(1);

        ResponseEntity<Object> result = brandController.updateBrand("ExistingBrand", new Brand("UpdatedName", "UpdatedPicture"));

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Brand info has been updated successfully", result.getBody());
    }

    @Test
    void testUpdateBrand_NotFound() {
        when(brandService.updateBrandInfo(anyString(), anyString(), anyString())).thenReturn(0);

        ResponseEntity<Object> result = brandController.updateBrand("NonexistentBrand", new Brand("UpdatedName", "UpdatedPicture"));

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Brand not found", result.getBody());
    }

    @Test
    void testAddBrand_ExceptionHandling() {
        when(brandValidator.validateBrand(any(Brand.class))).thenReturn(Collections.emptyList());
        when(userService.getUserByUsername(anyString())).thenReturn(new User());

        // Throw an exception in brandService.createBrand to test exception handling
        doThrow(new RuntimeException("Some error")).when(brandService).createBrand(any(Brand.class));

        ResponseEntity<Object> result = brandController.addBrand("john_doe", new Brand("Brand1", "Picture1"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("An error occurred while processing your request.", result.getBody());
    }
    @Test
    void testUpdateBrand_ExceptionHandling() {
        when(brandService.updateBrandInfo(anyString(), anyString(), anyString())).thenThrow(new RuntimeException("Some error"));

        ResponseEntity<Object> result = brandController.updateBrand("ExistingBrand", new Brand("UpdatedName", "UpdatedPicture"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("An error occurred while processing your request.", result.getBody());
    }


}