package at.technikum.springrestbackend.service;

import at.technikum.springrestbackend.model.Brand;
import at.technikum.springrestbackend.model.Phone;
import at.technikum.springrestbackend.repository.PhoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class PhoneServiceTest {
    @Mock
    private PhoneRepository phoneRepository;

    @InjectMocks
    private PhoneService phoneService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getPhonesShouldReturnAllPhones() {
        List<Phone> phonesList = Arrays.asList(new Phone(), new Phone());

        when(phoneRepository.findAll()).thenReturn(phonesList);

        List<Phone> foundPhones = phoneService.getPhones();

        assertNotNull(foundPhones);
        assertEquals(2, foundPhones.size());
    }

    @Test
    public void getPhoneShouldReturnPhoneWhenIdExists() {
        UUID id = UUID.randomUUID();
        Phone phone = new Phone();  // Erstellen eines neuen Phone-Objekts ohne ID

        when(phoneRepository.findById(id)).thenReturn(Optional.of(phone));  // Mocken der findById-Methode

        Phone found = phoneService.getPhone(id);  // Aufrufen der getPhone-Methode

        assertNotNull(found);  // Überprüfen, ob das Ergebnis nicht null ist
    }


    @Test
    public void getPhoneShouldThrowWhenIdDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(phoneRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            phoneService.getPhone(id);
        });
    }

    @Test
    public void createPhoneShouldPersistPhone() {
        Phone phone = new Phone();

        when(phoneRepository.save(phone)).thenReturn(phone);
        Phone createdPhone = phoneService.createPhone(phone);

        assertNotNull(createdPhone);
    }

    @Test
    public void deletePhoneShouldInvokeRepositoryDeleteMethod() {
        UUID id = UUID.randomUUID();
        doNothing().when(phoneRepository).deletePhoneById(id);

        phoneService.deletePhone(id);

        verify(phoneRepository, times(1)).deletePhoneById(id);
    }

    @Test
    public void updatePhoneInfoShouldUpdatePhoneDetails() {
        UUID oldId = UUID.randomUUID();
        String newName = "NewPhoneName";
        String newDescription = "NewDescription";
        float newDisplaySize = 5.5f;
        int newMemory = 128;
        int newBattery = 4000;
        float newPrice = 599.99f;
        when(phoneRepository.updatePhoneInfo(oldId, newName, newDescription, newDisplaySize, newMemory, newBattery, newPrice)).thenReturn(1);

        int updateCount = phoneService.updatePhoneInfo(oldId, newName, newDescription, newDisplaySize, newMemory, newBattery, newPrice);

        assertEquals(1, updateCount);


    }

    @Test
    public void getPhonesBrandShouldReturnPhonesForGivenBrand() {
        Brand brand = new Brand();
        List<Phone> phonesList = Arrays.asList(new Phone(), new Phone());

        when(phoneRepository.findByBrand(brand)).thenReturn(phonesList);

        List<Phone> foundPhones = phoneService.getPhonesBrand(brand);

        assertNotNull(foundPhones);
        assertEquals(2, foundPhones.size());
    }
    @Test
    public void getPhonesNameShouldReturnPhonesForGivenName() {
        String name = "SampleName";
        List<Phone> phonesList = Arrays.asList(new Phone(), new Phone());

        when(phoneRepository.findByName(name)).thenReturn(phonesList);

        List<Phone> foundPhones = phoneService.getPhonesName(name);

        assertNotNull(foundPhones);
        assertEquals(2, foundPhones.size());
    }

    @Test
    public void getPhonesDisplayShouldReturnPhonesForGivenDisplaySize() {
        float displaySize = 6.0f;
        List<Phone> phonesList = Arrays.asList(new Phone(), new Phone());

        when(phoneRepository.findByDisplaySize(displaySize)).thenReturn(phonesList);

        List<Phone> foundPhones = phoneService.getPhonesDisplay(displaySize);

        assertNotNull(foundPhones);
        assertEquals(2, foundPhones.size());
    }
    @Test
    public void deletePhoneShouldHandleExceptionWhenPhoneNotFound() {
        UUID id = UUID.randomUUID();

        doThrow(new NoSuchElementException("Phone not found")).when(phoneRepository).deletePhoneById(id);

        assertThrows(NoSuchElementException.class, () -> {
            phoneService.deletePhone(id);
        });
    }
    @Test
    public void testConstructorInjection() {
        assertNotNull(phoneService);
    }
    @Test
    public void getPhonesMemoryShouldReturnPhonesForGivenMemory() {
        int memory = 64;
        List<Phone> phonesList = Arrays.asList(new Phone(), new Phone());

        when(phoneRepository.findByMemory(memory)).thenReturn(phonesList);

        List<Phone> foundPhones = phoneService.getPhonesMemory(memory);

        assertNotNull(foundPhones);
        assertEquals(2, foundPhones.size());
    }

    @Test
    public void getPhonesBatteryShouldReturnPhonesForGivenBattery() {
        int battery = 4000;
        List<Phone> phonesList = Arrays.asList(new Phone(), new Phone());

        when(phoneRepository.findByBattery(battery)).thenReturn(phonesList);

        List<Phone> foundPhones = phoneService.getPhonesBattery(battery);

        assertNotNull(foundPhones);
        assertEquals(2, foundPhones.size());
    }

    @Test
    public void getPhonesPriceShouldReturnPhonesForGivenPrice() {
        float price = 499.99f;
        List<Phone> phonesList = Arrays.asList(new Phone(), new Phone());

        when(phoneRepository.findByPrice(price)).thenReturn(phonesList);

        List<Phone> foundPhones = phoneService.getPhonesPrice(price);

        assertNotNull(foundPhones);
        assertEquals(2, foundPhones.size());
    }
}
