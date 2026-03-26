package nl.miwnn.ch19.paul.skirental.model;

import jakarta.persistence.*;

/**
 * @author Paul Rademaker
 * A set of skis that the skirental has, that can be rented out to renters.
 */

@Entity
public class Copy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Ski ski;

    private Boolean available;

    public Copy(Ski ski) {
        this.ski = ski;
        this.available = true;
    }

    public Copy() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ski getSki() {
        return ski;
    }

    public void setSki(Ski ski) {
        this.ski = ski;
    }

    public Boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}





