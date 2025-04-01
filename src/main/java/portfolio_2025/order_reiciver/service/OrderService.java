package portfolio_2025.order_reiciver.service;

import org.springframework.stereotype.Service;
import portfolio_2025.order_reiciver.model.Order;
import portfolio_2025.order_reiciver.model.OrderItem;
import portfolio_2025.order_reiciver.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    
    // Create a new order in the system
    public Order createOrder(Order order) {
        // You can add business validations here if needed
        return orderRepository.save(order);
    }
    
    // Retrieve all orders from the database
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    // Retrieve a single order by its ID
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
    
    // Update an existing order
    public Order updateOrder(Order order) {
        // Optionally add validations or checks before updating
        return orderRepository.save(order);
    }
    
    // Delete an order by its ID
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
    
    // Add an OrderItem to an existing Order
    public Order addOrderItem(Long orderId, OrderItem orderItem) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.addOrderItem(orderItem);
            return orderRepository.save(order);
        } else {
            throw new RuntimeException("Order not found with id: " + orderId);
        }
    }
}