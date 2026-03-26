package nl.miwnn.ch19.paul.skirental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Paul Rademaker
 * ---- Programma dat dingen doet ----
 * ---- VERVANG MIJ ----
 */

@Entity
public class Huurder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Voornaam mag niet leeg zijn")
    private String firstName;

    @NotBlank(message = "Achternaam mag niet leeg zijn")
    private String lastName;

    public Huurder() {}

    @ManyToMany(mappedBy = "huurders", fetch = FetchType.EAGER)
    private List<Ski> skis = new ArrayList<>();


    public String getFullName() {
        return firstName + " " + lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
