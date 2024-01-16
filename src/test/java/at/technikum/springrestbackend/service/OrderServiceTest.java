package at.technikum.springrestbackend.service;

import at.technikum.springrestbackend.model.Orders;
import at.technikum.springrestbackend.model.User;
import at.technikum.springrestbackend.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OrderServiceTest {


    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetOrders() {
        List<Orders> mockOrders = new ArrayList<>();
        when(orderRepository.findAll()).thenReturn(mockOrders);

        List<Orders> result = orderService.getOrders();

        assertEquals(mockOrders, result);
    }

    @Test
    public void testGetOrder() {
        UUID orderId = UUID.randomUUID();
        Orders mockOrder = new Orders(orderId, new User(), new ArrayList<>());
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

        Orders result = orderService.getOrder(orderId);

        assertEquals(mockOrder, result);
    }

    @Test
    public void testGetOrdersUser() {
        User user = new User();
        List<Orders> mockOrders = new ArrayList<>();
        when(orderRepository.findByUser(user)).thenReturn(mockOrders);

        List<Orders> result = orderService.getOrdersUser(user);

        assertEquals(mockOrders, result);
    }

    @Test
    public void testCreateOrder() {
        Orders mockOrder = new Orders(UUID.randomUUID(), new User(), new ArrayList<>());
        when(orderRepository.save(mockOrder)).thenReturn(mockOrder);

        Orders result = orderService.createOrder(mockOrder);

        assertEquals(mockOrder, result);
    }

    @Test
    public void testDeleteOrder() {
        UUID orderId = UUID.randomUUID();

        // Überprüfen, ob die Methode deleteOrderById() aufgerufen wird
        doNothing().when(orderRepository).deleteOrderById(orderId);

        // Die Methode aufrufen, die getestet werden soll
        orderService.deleteOrder(orderId);

        // Überprüfen, ob die Methode deleteOrderById() mit der richtigen ID aufgerufen wurde
        verify(orderRepository).deleteOrderById(orderId);
    }

}


