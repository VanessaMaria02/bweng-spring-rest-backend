package at.technikum.springrestbackend.service;


import at.technikum.springrestbackend.controller.OrderController;
import at.technikum.springrestbackend.model.Orders;
import at.technikum.springrestbackend.model.Phone;
import at.technikum.springrestbackend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PhoneService phoneService;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetOrders() {
        List<Orders> mockOrders = Arrays.asList(new Orders(new User()), new Orders(new User()));
        when(orderService.getOrders()).thenReturn(mockOrders);

        List<Orders> result = orderController.getOrders();

        assertEquals(mockOrders, result);
    }

    @Test
    void testGetOrder_OrderExists() {
        Orders mockOrder = new Orders(new User());
        UUID orderId = UUID.randomUUID();
        when(orderService.getOrder(orderId)).thenReturn(mockOrder);

        Orders result = orderController.getOrder(orderId);

        assertEquals(mockOrder, result);
    }

    @Test
    void testGetOrder_OrderNotFound() {
        UUID orderId = UUID.randomUUID();
        when(orderService.getOrder(orderId)).thenReturn(null);

        Orders result = orderController.getOrder(orderId);

        assertEquals(null, result);
    }

    @Test
    void testCreateOrder_ValidOrder() {
        String username = "john_doe";
        User mockUser = new User();
        when(userService.getUserByUsername(username)).thenReturn(mockUser);

        List<UUID> phoneIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        Phone mockPhone1 = new Phone();
        Phone mockPhone2 = new Phone();
        when(phoneService.getPhone(any(UUID.class))).thenReturn(mockPhone1, mockPhone2);

        ResponseEntity<Object> result = orderController.createOrder(username, phoneIds);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("New Order is created.", result.getBody());
    }

    @Test
    void testCreateOrder_InvalidUser() {
        String username = "nonexistent_user";
        when(userService.getUserByUsername(username)).thenReturn(null);

        List<UUID> phoneIds = Collections.singletonList(UUID.randomUUID());

        ResponseEntity<Object> result = orderController.createOrder(username, phoneIds);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("No User with that username", result.getBody());
    }

    @Test
    void testCreateOrder_NullPhoneIds() {
        String username = "john_doe";
        User mockUser = new User();
        when(userService.getUserByUsername(username)).thenReturn(mockUser);

        ResponseEntity<Object> result = orderController.createOrder(username, null);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("At least one Phone is required", result.getBody());
    }

    @Test
    void testCreateOrder_PhoneNotFound() {
        String username = "john_doe";
        User mockUser = new User();
        when(userService.getUserByUsername(username)).thenReturn(mockUser);

        List<UUID> phoneIds = Collections.singletonList(UUID.randomUUID());
        when(phoneService.getPhone(any(UUID.class))).thenReturn(null);

        ResponseEntity<Object> result = orderController.createOrder(username, phoneIds);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("One of the phones was not found", result.getBody());
    }
}
