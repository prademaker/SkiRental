package nl.miwnn.ch19.paul.skirental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Paul Rademaker
 * Model voor een snowboard met alle attributen en koppelingen voor verhuur.
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

    private double dailyPrice;

    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String omschrijving;

    @ManyToMany
    private List<Type> types = new ArrayList<>();

    @OneToMany(mappedBy = "snowboard", cascade = CascadeType.ALL)
    private List<Copy> copies = new ArrayList<>();

    public Snowboard() {
    }

    public Snowboard(String merk, String model, double dailyPrice) {
        this.merk = merk;
        this.model = model;
        this.dailyPrice = dailyPrice;
    }

    public long getAantalBeschikbaar() {
        return copies.stream().filter(Copy::isAvailable).count();
    }

    public long getAantalUitgeleend() {
        return copies.stream().filter(c -> !c.isAvailable()).count();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMerk() { return merk; }
    public void setMerk(String merk) { this.merk = merk; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public double getDailyPrice() { return dailyPrice; }
    public void setDailyPrice(double dailyPrice) { this.dailyPrice = dailyPrice; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getOmschrijving() { return omschrijving; }
    public void setOmschrijving(String omschrijving) { this.omschrijving = omschrijving; }

    public List<Type> getTypes() { return types; }
    public void setTypes(List<Type> types) { this.types = types; }

    public List<Copy> getCopies() { return copies; }
    public void setCopies(List<Copy> copies) { this.copies = copies; }
}