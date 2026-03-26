package nl.miwnn.ch19.paul.skirental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Paul Rademaker
 * ---- VERVANG MIJ ----
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"merk", "model"}))
public class Ski {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Merk mag niet leeg zijn")
    private String merk;

    @NotBlank(message = "Model mag niet leeg zijn")
    private String model;

    @NotEmpty(message = "Type mag niet leeg zijn")
    @ManyToMany
    private List<Type> types = new ArrayList<>();

    @OneToMany(mappedBy = "ski", cascade = CascadeType.ALL)
    private List<Copy> copies = new ArrayList<>();

    @ManyToMany
    private List<Huurder> huurders = new ArrayList<>();

    public Ski(String merk, String model) {
        this.merk = merk;
        this.model = model;
    }

    public Ski() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Copy> getCopies() {
        return copies;
    }

    public void setCopies(List<Copy> copies) {
        this.copies = copies;
    }

    public List<Huurder> getHuurders() {
        return huurders;
    }

    public void setHuurders(List<Huurder> huurders) {
        this.huurders = huurders;
    }

    public long getAantalBeschikbaar() {
        return copies.stream().filter(Copy::isAvailable).count();
    }

    public long getAantalUitgeleend() {
        return copies.stream().filter(c -> !c.isAvailable()).count();
    }

    public List<Type> getTypes() { return types; }
    public void setTypes(List<Type> types) { this.types = types; }

}

