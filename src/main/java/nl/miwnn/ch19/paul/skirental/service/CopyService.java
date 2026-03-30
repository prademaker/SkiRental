package nl.miwnn.ch19.paul.skirental.service;

import nl.miwnn.ch19.paul.skirental.model.Copy;
import nl.miwnn.ch19.paul.skirental.model.Ski;
import nl.miwnn.ch19.paul.skirental.model.Snowboard;
import nl.miwnn.ch19.paul.skirental.repository.CopyRepository;
import nl.miwnn.ch19.paul.skirental.repository.SkiRepository;
import nl.miwnn.ch19.paul.skirental.repository.SnowboardRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CopyService {

    private final CopyRepository copyRepository;
    private final SkiRepository skiRepository;
    private final SnowboardRepository snowboardRepository;

    public CopyService(CopyRepository copyRepository,
                       SkiRepository skiRepository,
                       SnowboardRepository snowboardRepository) {
        this.copyRepository = copyRepository;
        this.skiRepository = skiRepository;
        this.snowboardRepository = snowboardRepository;
    }

    public Optional<Copy> findById(Long id) {
        return copyRepository.findById(id);
    }

    public Copy borrowCopy(Long copyId) {
        Copy copy = copyRepository.findById(copyId)
                .orElseThrow(() -> new IllegalArgumentException("Exemplaar niet gevonden"));

        if (!copy.isAvailable()) {
            throw new IllegalStateException("Exemplaar is al uitgeleend");
        }

        copy.setAvailable(false);
        return copyRepository.save(copy);
    }

    public Copy returnCopy(Long copyId) {
        Copy copy = copyRepository.findById(copyId)
                .orElseThrow(() -> new IllegalArgumentException("Exemplaar niet gevonden"));

        if (copy.isAvailable()) {
            throw new IllegalStateException("Exemplaar was niet uitgeleend");
        }

        copy.setAvailable(true);
        return copyRepository.save(copy);
    }

    public void addSkiCopy(Long skiId) {
        Ski ski = skiRepository.findById(skiId)
                .orElseThrow(() -> new IllegalArgumentException("Ski niet gevonden"));
        copyRepository.save(new Copy(ski));
    }

    public void addSnowboardCopy(Long snowboardId) {
        Snowboard snowboard = snowboardRepository.findById(snowboardId)
                .orElseThrow(() -> new IllegalArgumentException("Snowboard niet gevonden"));
        copyRepository.save(new Copy(snowboard));
    }

    public void deleteCopy(Long id) {
        copyRepository.deleteById(id);
    }
}