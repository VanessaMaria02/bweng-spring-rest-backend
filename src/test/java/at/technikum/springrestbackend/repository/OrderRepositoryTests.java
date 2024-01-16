package at.technikum.springrestbackend.repository;

import at.technikum.springrestbackend.model.Orders;
import at.technikum.springrestbackend.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderRepositoryTests {

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        List<Orders> mockOrders = Arrays.asList(new Orders(), new Orders());
        when(orderRepository.findAll()).thenReturn(mockOrders);

        List<Orders> orders = orderRepository.findAll();
        assertNotNull(orders);
        assertEquals(2, orders.size());

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    public void testFindByUser() {
        User user = new User();
        List<Orders> mockOrders = Arrays.asList(new Orders(), new Orders());
        when(orderRepository.findByUser(user)).thenReturn(mockOrders);

        List<Orders> orders = orderRepository.findByUser(user);
        assertNotNull(orders);
        assertEquals(2, orders.size());

        verify(orderRepository, times(1)).findByUser(user);
    }

    @Test
    public void testDeleteOrderById() {
        doNothing().when(orderRepository).deleteOrderById(any(UUID.class));

        UUID orderId = UUID.randomUUID();
        orderRepository.deleteOrderById(orderId);

        verify(orderRepository, times(1)).deleteOrderById(orderId);
    }


}

