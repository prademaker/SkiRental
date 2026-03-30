package nl.miwnn.ch19.paul.skirental.service;

import nl.miwnn.ch19.paul.skirental.model.Ski;
import nl.miwnn.ch19.paul.skirental.model.Type;
import nl.miwnn.ch19.paul.skirental.repository.SkiRepository;
import nl.miwnn.ch19.paul.skirental.repository.TypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkiService {

    private final SkiRepository skiRepository;
    private final TypeService typeService;

    public SkiService(SkiRepository skiRepository, TypeService typeService) {
        this.skiRepository = skiRepository;
        this.typeService = typeService;
    }

    public List<Ski> getSkis(String query) {
        if (query != null && !query.isBlank()) {
            return skiRepository.findByMerkContainingIgnoreCase(query);
        }
        return skiRepository.findAll();
    }

    public Optional<Ski> findById(Long id) {
        return skiRepository.findById(id);
    }

    public List<Type> getAllTypes() {
        return typeService.findAll();
    }

    public void deleteById(Long id) {
        skiRepository.deleteById(id);
    }

    /**
     * Controleert of een ski-combinatie al bestaat,
     * rekening houdend met het feit of we een bestaande ski bewerken.
     */
    public boolean isDuplicate(Ski ski) {
        Optional<Ski> bestaande = skiRepository.findByMerkAndModel(ski.getMerk(), ski.getModel());
        return bestaande.filter(value -> !value.getId().equals(ski.getId())).isPresent();
    }

    public void save(Ski ski) {
        skiRepository.save(ski);
    }
}