package nl.miwnn.ch19.paul.skirental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * @author Paul Rademaker
 * ---- VERVANG MIJ ----
 */
@Entity
public class Ski {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Merk mag niet leeg zijn")
    private String merk;

    @NotBlank(message = "Model mag niet leeg zijn")
    private String model;

    @NotBlank(message = "Type mag niet leeg zijn")
    private String type;

    public Ski(String merk, String model, String type) {
        this.merk = merk;
        this.model = model;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

