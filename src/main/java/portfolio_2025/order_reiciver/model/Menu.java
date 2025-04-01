package portfolio_2025.order_reiciver.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "menu")
public class Menu {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String option;
    private String note;

    // Constructors
    public Menu() {
    }

    public Menu(String name, String option, String note) {
        this.name = name;
        this.option = option;
        this.note = note;
    }

    // Getters and setters (or use Lombok @Data / @Getter / @Setter)
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOption() {
        return option;
    }

    public String getNote() {
        return note;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public void setNote(String note) {
        this.note = note;
    }
}