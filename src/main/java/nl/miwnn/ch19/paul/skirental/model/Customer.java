package nl.miwnn.ch19.paul.skirental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Paul Rademaker
 * Representeert een klant (huurder) van de skiverhuur.
 */
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Voornaam is verplicht")
    private String firstName;

    @NotBlank(message = "Achternaam is verplicht")
    private String lastName;

    @Email(message = "Voer een geldig e-mailadres in")
    private String email;

    private int heightCm;
    private double weightKg;
    private int shoeSize;

    @OneToMany(mappedBy = "customer")
    private List<Rental> rentals = new ArrayList<>();

    public Customer() {
    }

    public Customer(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Customer(String firstName, String lastName, String email,
                    int heightCm, double weightKg, int shoeSize) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.heightCm = heightCm;
        this.weightKg = weightKg;
        this.shoeSize = shoeSize;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getHeightCm() { return heightCm; }
    public void setHeightCm(Integer heightCm) { this.heightCm = heightCm; }

    public Double getWeightKg() { return weightKg; }
    public void setWeightKg(Double weightKg) { this.weightKg = weightKg; }

    public Integer getShoeSize() { return shoeSize; }
    public void setShoeSize(Integer shoeSize) { this.shoeSize = shoeSize; }

    public List<Rental> getRentals() { return rentals; }
    public void setRentals(List<Rental> rentals) { this.rentals = rentals; }
}