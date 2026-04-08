package nl.miwnn.ch19.paul.skirental.model;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * @author Paul Rademaker
 * ---- Programma dat dingen doet ----
 * ---- VERVANG MIJ ----
 */
@Entity
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer customer; // Veel verhuurcontracten voor één klant

    @ManyToOne
    private Copy copy; // Veel verhuurcontracten (historie) voor één exemplaar

    private LocalDate startDate;
    private LocalDate endDate;
    private double totalPrice;
    private boolean returned = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Copy getCopy() {
        return copy;
    }

    public void setCopy(Copy copy) {
        this.copy = copy;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }
}