package nl.miwnn.ch19.paul.skirental.service;

import nl.miwnn.ch19.paul.skirental.model.Snowboard;
import nl.miwnn.ch19.paul.skirental.model.Type;
import nl.miwnn.ch19.paul.skirental.repository.SnowboardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SnowboardService {

    private final SnowboardRepository snowboardRepository;
    private final TypeService typeService;

    public SnowboardService(SnowboardRepository snowboardRepository, TypeService typeService) {
        this.snowboardRepository = snowboardRepository;
        this.typeService = typeService;
    }

    public List<Snowboard> getSnowboards(String query) {
        if (query != null && !query.isBlank()) {
            return snowboardRepository.findByMerkContainingIgnoreCase(query);
        }
        return snowboardRepository.findAll();
    }

    public List<Snowboard> findAll() {
        return snowboardRepository.findAll();
    }

    public Optional<Snowboard> findById(Long id) {
        return snowboardRepository.findById(id);
    }

    public List<Type> getAllTypes() {
        return typeService.findAll();
    }

    public void deleteById(Long id) {
        snowboardRepository.deleteById(id);
    }

    public void save(Snowboard snowboard) {
        snowboardRepository.save(snowboard);
    }

    public boolean isDuplicate(Snowboard snowboard) {
        Optional<Snowboard> bestaande = snowboardRepository.findByMerkIgnoreCaseAndModelIgnoreCase(
                snowboard.getMerk(), snowboard.getModel());

        if (bestaande.isPresent()) {
            if (snowboard.getId() == null) {
                return true;
            }
            return !bestaande.get().getId().equals(snowboard.getId());
        }

        return false;
    }
}