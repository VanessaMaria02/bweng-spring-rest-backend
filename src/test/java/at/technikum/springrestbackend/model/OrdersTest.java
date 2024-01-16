package at.technikum.springrestbackend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class OrdersTest {

    @Mock
    private User mockedUser;

    @Mock
    private Phone mockedPhone1;

    @Mock
    private Phone mockedPhone2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testAddPhoneToOrders() {
        Orders orders = new Orders(mockedUser);
        List<Phone> phones = new ArrayList<>();
        phones.add(mockedPhone1);
        phones.add(mockedPhone2);
        orders.addPhone(mockedPhone1);
        orders.addPhone(mockedPhone2);

        assertNotNull(orders.getPhones());
        assertEquals(phones.size(), orders.getPhones().size());
        assertTrue(orders.getPhones().contains(mockedPhone1));
        assertTrue(orders.getPhones().contains(mockedPhone2));
    }

}