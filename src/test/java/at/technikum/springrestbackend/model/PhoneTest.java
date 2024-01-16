package at.technikum.springrestbackend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PhoneTest {

    @Mock
    private Brand mockedBrand;

    @Mock
    private User mockedUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPhoneConstructorWithAllParameters() {
        Instant now = Instant.now();
        Phone phone = new Phone("PhoneName", "Description", 6.2f, 64, 4000, 599.99f, mockedBrand, mockedUser, "phone.jpg");

        assertNotNull(phone);
        assertEquals("PhoneName", phone.getName());
        assertEquals("Description", phone.getDescription());
        assertEquals(6.2f, phone.getDisplaySize());
        assertEquals(64, phone.getMemory());
        assertEquals(4000, phone.getBattery());
        assertEquals(599.99f, phone.getPrice());
        assertEquals(mockedBrand, phone.getBrand());
        assertEquals(mockedUser, phone.getCreatedBy());
        assertEquals("phone.jpg", phone.getPicture());
    }

    @Test
    public void testPhoneConstructorWithRequiredParameters() {
        Phone phone = new Phone("PhoneName", "Description", 6.2f, 64, 4000, 599.99f);

        assertNotNull(phone);
        assertEquals("PhoneName", phone.getName());
        assertEquals("Description", phone.getDescription());
        assertEquals(6.2f, phone.getDisplaySize());
        assertEquals(64, phone.getMemory());
        assertEquals(4000, phone.getBattery());
        assertEquals(599.99f, phone.getPrice());
        assertNull(phone.getBrand());
        assertNull(phone.getCreatedBy());
        assertNull(phone.getPicture());
    }
    @Test
    public void testCreatedOnAndLastUpdatedOn() {
        Instant now = Instant.now();
        Phone phone = new Phone("PhoneName", "Description", 6.2f, 64, 4000, 599.99f, mockedBrand, mockedUser, "phone.jpg");
        phone.setCreatedOn(now);
        phone.setLastUpdatedOn(now);

        assertNotNull(phone.getCreatedOn());
        assertNotNull(phone.getLastUpdatedOn());
        assertEquals(now, phone.getCreatedOn());
        assertEquals(now, phone.getLastUpdatedOn());
    }
}
