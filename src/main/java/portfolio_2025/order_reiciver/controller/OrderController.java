package portfolio_2025.order_reiciver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import portfolio_2025.order_reiciver.model.Menu;
import portfolio_2025.order_reiciver.model.Order;
import portfolio_2025.order_reiciver.model.OrderItem;
import portfolio_2025.order_reiciver.repository.MenuRepository;
import portfolio_2025.order_reiciver.service.OrderService;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final MenuRepository menuRepository;

    @Autowired
    public OrderController(OrderService orderService, MenuRepository menuRepository) {
        this.orderService = orderService;
        this.menuRepository = menuRepository;
    }

    // Display list of orders
    @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "orders"; // Thymeleaf template name for order list
    }

    // Show form to create a new order
    @GetMapping("/create")
    public String showCreateOrderForm(Model model) {
        List<Menu> menus = menuRepository.findAll();
        model.addAttribute("menuList", menus);
        return "create-order"; // Thymeleaf template for creating an order
    }

    // Process order creation
    @PostMapping("/create")
    public String createOrder(@RequestParam("menuId") int menuId,
                              @RequestParam("quantity") int quantity,
                              @RequestParam("selectedOption") String selectedOption,
                              @RequestParam("addedNote") String addedNote) {
        Order order = new Order();
        Menu menu = menuRepository.findById(menuId).orElse(null);
        if (menu != null) {
            OrderItem orderItem = new OrderItem(menu, quantity, selectedOption, addedNote);
            order.addOrderItem(orderItem);
            orderService.createOrder(order);
        }
        return "redirect:/orders";
    }

    // View details of a specific order
    @GetMapping("/{orderId}")
    public String viewOrder(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId).orElse(null);
        model.addAttribute("order", order);
        return "view-order"; // Thymeleaf template for order details
    }
}