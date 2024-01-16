package at.technikum.springrestbackend.Controller;


import at.technikum.springrestbackend.controller.OrderController;
import at.technikum.springrestbackend.model.Orders;
import at.technikum.springrestbackend.model.Phone;
import at.technikum.springrestbackend.model.User;
import at.technikum.springrestbackend.service.OrderService;
import at.technikum.springrestbackend.service.PhoneService;
import at.technikum.springrestbackend.service.UserService;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
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

    private Instant instant;

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
    @Test
    void testGetOrder() {
        UUID orderId = UUID.randomUUID();
        Orders mockOrder = new Orders();
        when(orderService.getOrder(orderId)).thenReturn(mockOrder);

        Orders result = orderController.getOrder(orderId);

        assertEquals(mockOrder, result);
    }
    @Test
    void testGetOrdersUsers() {
        User user = new User();
        List<Orders> mockOrders = Arrays.asList(new Orders(), new Orders());
        when(orderService.getOrdersUser(user)).thenReturn(mockOrders);

        List<Orders> result = orderController.getOrdersUsers(user);

        assertEquals(mockOrders, result);
    }
    @Test
    void testCreateOrder() {
        String username = "john_doe";
        List<UUID> phoneIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());


        ResponseEntity<Object> expectedResponse = new ResponseEntity<>("New Order is created.", HttpStatus.CREATED);

        when(userService.getUserByUsername(username)).thenReturn(new User());
        when(phoneService.getPhone(any(UUID.class))).thenReturn(new Phone());

        ResponseEntity<Object> result = orderController.createOrder(username, phoneIds);

        assertEquals(expectedResponse, result);
    }
    @Test
    void testHandleOrderCreation_TokenExpiredException() {
        String username = "john_doe";
        List<UUID> phoneIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());

        when(userService.getUserByUsername(username)).thenReturn(new User());
        when(phoneService.getPhone(any(UUID.class))).thenReturn(new Phone());
        doThrow(new TokenExpiredException("Expired", instant)).when(orderService).createOrder(any(Orders.class));

        ResponseEntity<Object> result = orderController.createOrder(username, phoneIds);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("The JWT Token is expired, pleas login in again", result.getBody());
    }

    @Test
    void testHandleOrderCreation_GenericException() {
        String username = "john_doe";
        List<UUID> phoneIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());

        when(userService.getUserByUsername(username)).thenReturn(new User());
        when(phoneService.getPhone(any(UUID.class))).thenReturn(new Phone());
        doThrow(new RuntimeException("Some error")).when(orderService).createOrder(any(Orders.class));

        ResponseEntity<Object> result = orderController.createOrder(username, phoneIds);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("An error occurred while processing your request.", result.getBody());
    }


}