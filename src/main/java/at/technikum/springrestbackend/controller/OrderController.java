package at.technikum.springrestbackend.controller;
import at.technikum.springrestbackend.model.Orders;
import at.technikum.springrestbackend.model.Phone;
import at.technikum.springrestbackend.model.User;
import at.technikum.springrestbackend.security.JwtToPrincipalConverter;
import at.technikum.springrestbackend.service.OrderService;
import at.technikum.springrestbackend.service.PhoneService;
import at.technikum.springrestbackend.service.UserService;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.*;

@RestController
@CrossOrigin(origins = "*")
public class OrderController {

    private final UserService userService;

    private final PhoneService phoneService;
    private final OrderService orderService;

    public OrderController(UserService userService, PhoneService phoneService, OrderService orderService) {
        this.userService = userService;
        this.phoneService = phoneService;
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


    @PostMapping("/createOrder/{username}")
    public ResponseEntity<Object> createOrder(@PathVariable @Valid String username,
            @RequestBody List<UUID> phones) {
        return handelOrderCreation(username, phones);
    }

    @DeleteMapping("/deleteOrder/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable UUID orderId) {

        return handelOrderDeletion(orderId);

    }

    private ResponseEntity<String> handelOrderDeletion(UUID orderId) {
        String username = JwtToPrincipalConverter.getCurrentUsername();
        String userRole = JwtToPrincipalConverter.getCurrentUserRole();

        try{
            Orders order = orderService.getOrder(orderId);
            if(order == null){
                return new ResponseEntity<>("There is no Order with this ID", HttpStatus.NOT_FOUND);
            }

            User user = order.getUser();
            String orderUserName = user.getUsername();

            if(!Objects.equals(username, orderUserName) && !userRole.equals("ROLE_admin")){
                return new ResponseEntity<>("Only Admins or the creator of the order can delete it.", HttpStatus.UNAUTHORIZED);
            }
        }catch (NoSuchElementException e){
            return new ResponseEntity<>("No Order with this Id found", HttpStatus.NOT_FOUND);
        }


        try{
            orderService.deleteOrder(orderId);
            return new ResponseEntity<>("Order deleted successfully", HttpStatus.OK);
        }catch (DataIntegrityViolationException e) {
            // Handle foreign key constraint violation
            return new ResponseEntity<>("Cannot delete the order. It has associated items.", HttpStatus.CONFLICT);
        }catch (TokenExpiredException e){
            return new ResponseEntity<>("The JWT Token is expired, pleas login in again", HttpStatus.UNAUTHORIZED);
        }catch (Exception e) {
            // Handle other exceptions
            return new ResponseEntity<>("An error occurred while processing your request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Object> handelOrderCreation(String username, List<UUID> phoneIds) {
        User user = userService.getUserByUsername(username);

        if (user == null) {
            return new ResponseEntity<>("No User with that username", HttpStatus.BAD_REQUEST);
        }

        if(phoneIds == null){
            return new ResponseEntity<>("At least one Phone is required", HttpStatus.BAD_REQUEST);
        }
        User mangeduser = userService.getUser(user.getId());

        Orders newOrder = new Orders(mangeduser);

        List<Phone> phones = new ArrayList<>();

        for (UUID phoneId : phoneIds) {
            Phone phone = phoneService.getPhone(phoneId);

            if (phone != null) {
                phones.add(phone);
                newOrder.addPhone(phone);
            } else {
                // Handle the case where a phone is not found for a given ID
                return new ResponseEntity<>("One of the phones was not found", HttpStatus.BAD_REQUEST);
            }
        }

        try{
            orderService.createOrder(newOrder);
            return new ResponseEntity<>("New Order is created.", HttpStatus.CREATED);
        }catch (TokenExpiredException e){
            return new ResponseEntity<>("The JWT Token is expired, pleas login in again", HttpStatus.UNAUTHORIZED);
        }catch (Exception e) {
            // Handle other exceptions
            return new ResponseEntity<>("An error occurred while processing your request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }




}
