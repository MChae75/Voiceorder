package portfolio_2025.order_reiciver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import portfolio_2025.order_reiciver.model.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    // Optionally define custom query methods here
    // List<Menu> findByNameContaining(String keyword);
}