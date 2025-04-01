package portfolio_2025.order_reiciver.model;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link back to the Order
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // The menu item that was ordered
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private int quantity;
    private String selectedOption;
    private String addedNote;

    // Constructors
    public OrderItem() {}

    public OrderItem(Menu menu, int quantity, String selectedOption, String addedNote) {
        this.menu = menu;
        this.quantity = quantity;
        this.selectedOption = selectedOption;
        this.addedNote = addedNote;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public String getAddedNote() {
        return addedNote;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public void setAddedNote(String addedNote) {
        this.addedNote = addedNote;
    }
}