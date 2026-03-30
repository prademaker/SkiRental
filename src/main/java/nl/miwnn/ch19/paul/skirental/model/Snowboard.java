package nl.miwnn.ch19.paul.skirental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Paul Rademaker
 * Model snowboard about all attributes a board has
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"merk", "model"}))
public class Snowboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Merk mag niet leeg zijn")
    private String merk;

    @NotBlank(message = "Model mag niet leeg zijn")
    private String model;

    @ManyToMany
    private List<Type> types = new ArrayList<>();

    @OneToMany(mappedBy = "snowboard", cascade = CascadeType.ALL)
    private List<Copy> copies = new ArrayList<>();

    public Snowboard(Long id, String merk, String model, List<Type> types, List<Copy> copies) {
        this.id = id;
        this.merk = merk;
        this.model = model;
        this.types = types;
        this.copies = copies;
    }

    public Snowboard() {
    }

    public long getAantalBeschikbaar() {
        return copies.stream().filter(Copy::isAvailable).count();
    }

    public long getAantalUitgeleend() {
        return copies.stream().filter(c -> !c.isAvailable()).count();
    }
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

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    public List<Copy> getCopies() {
        return copies;
    }

    public void setCopies(List<Copy> copies) {
        this.copies = copies;
    }
}