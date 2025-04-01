package portfolio_2025.order_reiciver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import portfolio_2025.order_reiciver.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Additional custom queries can be added here if needed
}
