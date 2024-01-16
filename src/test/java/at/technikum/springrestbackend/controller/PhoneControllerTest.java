package at.technikum.springrestbackend.controller;

import at.technikum.springrestbackend.model.Brand;
import at.technikum.springrestbackend.model.Phone;
import at.technikum.springrestbackend.model.User;
import at.technikum.springrestbackend.service.BrandService;
import at.technikum.springrestbackend.service.PhoneService;
import at.technikum.springrestbackend.service.UserService;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PhoneControllerTest {

    @Mock
    private PhoneService phoneService;

    @Mock
    private UserService userService;

    @Mock
    private BrandService brandService;

    @InjectMocks
    private PhoneController phoneController;

    private Instant instant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetPhones() {
        List<Phone> mockPhones = Arrays.asList(
                new Phone("Phone1", "Desc1", 5.0f, 4, 3000, 500.0f),
                new Phone("Phone2", "Desc2", 6.0f, 8, 4000, 700.0f)
        );

        when(phoneService.getPhones()).thenReturn(mockPhones);

        List<Phone> result = phoneController.getPhones();

        assertEquals(mockPhones, result);
    }

    @Test
    void testGetPhoneById() {
        UUID phoneId = UUID.randomUUID();
        Phone mockPhone = new Phone("Phone1", "Desc1", 5.0f, 4, 3000, 500.0f);
        when(phoneService.getPhone(phoneId)).thenReturn(mockPhone);

        Phone result = phoneController.getPhone(phoneId);

        assertEquals(mockPhone, result);
    }

    @Test
    void testCreatePhone_Success() {
        String username = "user1";
        String brandName = "Brand1";
        Phone newPhone = new Phone("Phone1", "Desc1", 5.0f, 4, 3000, 500.0f);

        when(userService.getUserByUsername(username)).thenReturn(new User());
        when(brandService.getBrandByname(brandName)).thenReturn(new Brand());

        ResponseEntity<Object> response = phoneController.createPhone(username, brandName, newPhone);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("New phone is saved.", response.getBody());
    }

    @Test
    void testGetPhonesByName() {
        String name = "Name1";
        List<Phone> mockPhones = Arrays.asList(new Phone(), new Phone());
        when(phoneService.getPhonesName(name)).thenReturn(mockPhones);

        List<Phone> result = phoneController.getPhonesName(name);

        assertEquals(mockPhones, result);
    }

    @Test
    void testGetPhonesByDisplaySize() {
        float displaySize = 5.5f;
        List<Phone> mockPhones = Arrays.asList(new Phone(), new Phone());
        when(phoneService.getPhonesDisplay(displaySize)).thenReturn(mockPhones);
        List<Phone> result = phoneController.getPhoneDisplay(displaySize);

        assertEquals(mockPhones, result);
    }

    @Test
    void testGetPhonesByBattery() {
        int battery = 3000;
        List<Phone> mockPhones = Arrays.asList(new Phone(), new Phone());
        when(phoneService.getPhonesBattery(battery)).thenReturn(mockPhones);

        List<Phone> result = phoneController.getPhonesBattery(battery);

        assertEquals(mockPhones, result);
    }

    @Test
    void testGetPhonesByPrice() {
        float price = 500.0f;
        List<Phone> mockPhones = Arrays.asList(new Phone(), new Phone());
        when(phoneService.getPhonesPrice(price)).thenReturn(mockPhones);

        List<Phone> result = phoneController.getPhonePrice(price);
        assertEquals(mockPhones, result);
    }

    @Test
    void testGetPhonesByBrand() {
        Brand mockBrand = new Brand();
        List<Phone> mockPhones = Arrays.asList(new Phone(), new Phone());
        when(phoneService.getPhonesBrand(mockBrand)).thenReturn(mockPhones);

        List<Phone> result = phoneController.getPhonesBrand(mockBrand);

        assertEquals(mockPhones, result);
    }


    @Test
    void testGetPhonesByMemory() {
        int memory = 4;
        List<Phone> mockPhones = Arrays.asList(new Phone(), new Phone());
        when(phoneService.getPhonesMemory(memory)).thenReturn(mockPhones);

        List<Phone> result = phoneController.getPhonesMemory(memory);

        assertEquals(mockPhones, result);
    }

    @Test
    void testCreatePhone_UserNotFound() {
        String username = "nonexistentUser";
        String brandName = "Brand1";
        Phone phone = new Phone();

        when(userService.getUserByUsername(username)).thenReturn(null);

        ResponseEntity<Object> response = phoneController.createPhone(username, brandName, phone);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No User with that username", response.getBody());
    }

    @Test
    void testDeletePhone_NotFound() {
        UUID id = UUID.randomUUID();

        when(phoneService.getPhone(id)).thenReturn(null);

        ResponseEntity<Object> response = phoneController.deletePhone(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Phone not found", response.getBody());
    }

    @Test
    void testDeletePhone_DataIntegrityViolationException() {
        UUID phoneId = UUID.randomUUID();
        Phone phone = new Phone();

        when(phoneService.getPhone(phoneId)).thenReturn(phone);
        doThrow(new DataIntegrityViolationException("Integrity violation")).when(phoneService).deletePhone(phone.getId());

        ResponseEntity<Object> response = phoneController.deletePhone(phoneId);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Cannot delete the phone. It has associated orders.", response.getBody());
    }

    @Test
    void testDeletePhone_GenericException() {
        UUID phoneId = UUID.randomUUID();
        Phone phone = new Phone();

        when(phoneService.getPhone(phoneId)).thenReturn(phone);
        doThrow(new RuntimeException("Some error")).when(phoneService).deletePhone(phone.getId());

        ResponseEntity<Object> response = phoneController.deletePhone(phoneId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while processing your request.", response.getBody());
    }
    @Test
    void testCreatePhone_BrandNotFound() {
        String username = "user1";
        String brandName = "nonexistentBrand";
        Phone phone = new Phone();

        when(userService.getUserByUsername(username)).thenReturn(new User());
        when(brandService.getBrandByname(brandName)).thenReturn(null);

        ResponseEntity<Object> response = phoneController.createPhone(username, brandName, phone);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No Brand with that name", response.getBody());
    }

    @Test
    void testHandlePhoneCreation_TokenExpiredException() {
        String username = "user1";
        String brandName = "Brand1";
        Phone newPhone = new Phone();

        when(userService.getUserByUsername(username)).thenReturn(new User());
        when(brandService.getBrandByname(brandName)).thenReturn(new Brand());
        doThrow(new TokenExpiredException("Expired", instant)).when(phoneService).createPhone(newPhone);

        ResponseEntity<Object> response = phoneController.createPhone(username, brandName, newPhone);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("The JWT Token is expired, pleas login in again", response.getBody());
    }

    @Test
    void testHandlePhoneCreation_GenericException() {
        String username = "user1";
        String brandName = "Brand1";
        Phone newPhone = new Phone();

        when(userService.getUserByUsername(username)).thenReturn(new User());
        when(brandService.getBrandByname(brandName)).thenReturn(new Brand());
        doThrow(new RuntimeException("Some error")).when(phoneService).createPhone(newPhone);

        ResponseEntity<Object> response = phoneController.createPhone(username, brandName, newPhone);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while processing your request.", response.getBody());
    }
    @Test
    void testUpdatePhone_NotFound() {
        UUID phoneId = UUID.randomUUID();
        Phone updatedPhone = new Phone();

        when(phoneService.updatePhoneInfo(eq(phoneId), any(), any(), anyInt(), anyInt(), anyInt(), anyFloat()))
                .thenReturn(0);

        ResponseEntity<Object> response = phoneController.updatePhone(phoneId, updatedPhone);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Phone not found", response.getBody());
    }
    @Test
    void testDeletePhone_Success() {
        UUID phoneId = UUID.randomUUID();
        Phone mockPhone = new Phone();

        when(phoneService.getPhone(phoneId)).thenReturn(mockPhone);
        doNothing().when(phoneService).deletePhone(phoneId);

        ResponseEntity<Object> response = phoneController.deletePhone(phoneId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Phone deleted successfully", response.getBody());
    }
}
