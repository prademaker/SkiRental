package nl.miwnn.ch19.paul.skirental.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String naam;

    @ManyToMany(mappedBy = "types")
    private List<Ski> skis = new ArrayList<>();

    public Type() {}

    public Type(String naam) {
        this.naam = naam;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNaam() { return naam; }
    public void setNaam(String naam) { this.naam = naam; }
    public List<Ski> getSkis() { return skis; }
    public void setSkis(List<Ski> skis) { this.skis = skis; }
}