package at.technikum.springrestbackend.service;

import at.technikum.springrestbackend.model.Orders;
import at.technikum.springrestbackend.model.User;
import at.technikum.springrestbackend.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Orders> getOrders() {
        return orderRepository.findAll();
    }

    public Orders getOrder(UUID id) {
        return orderRepository.findById(id).orElseThrow();
    }

    public List<Orders> getOrdersUser(User user) {
        return orderRepository.findByUser(user);
    }

    public Orders createOrder(Orders order) {
        return orderRepository.save(order);
    }
    public void deleteOrder(UUID id) {
        orderRepository.deleteOrderById(id);
    }

}
