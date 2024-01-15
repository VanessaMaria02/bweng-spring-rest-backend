package at.technikum.springrestbackend.controller;
import at.technikum.springrestbackend.model.Brand;
import at.technikum.springrestbackend.model.Orders;
import at.technikum.springrestbackend.model.User;
import at.technikum.springrestbackend.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public List<Orders> getOrders() {
        return orderService.getOrders();
    }

    @GetMapping("/order/{id}")
    public Orders getOrder(@PathVariable UUID id) {
        return orderService.getOrder(id);
    }
    @GetMapping("/orders/{user}")
    public List<Orders> getOrdersUsers(@PathVariable User user){
        return orderService.getOrdersUser(user);
    }
    @PostMapping("/createOrder")
    public ResponseEntity<Orders> createOrder(@RequestBody Orders order) {
        Orders createdOrder = orderService.createOrder(order);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }
    @DeleteMapping("/deleteOrder/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable UUID orderId) {
        orderService.deleteOrder(orderId);
        return new ResponseEntity<>("Order deleted successfully", HttpStatus.OK);
    }
}
