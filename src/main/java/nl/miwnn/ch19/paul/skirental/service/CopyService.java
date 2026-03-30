package nl.miwnn.ch19.paul.skirental.service;

import nl.miwnn.ch19.paul.skirental.model.Copy;
import nl.miwnn.ch19.paul.skirental.repository.CopyRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Paul Rademaker
* handle all business logic regarding copies
 */

@Service
public class CopyService {

    private final CopyRepository copyRepository;

    public CopyService(CopyRepository copyRepository) {
        this.copyRepository = copyRepository;
    }

    public void borrowCopy(Long copyId) {
        extracted(copyId);
    }

    private void extracted(Long copyId) {
        Optional<Copy> opionalCopy = copyRepository.findById(copyId);

        if (opionalCopy.isEmpty()) {
            throw new IllegalArgumentException(String.format("Exemplaar met id %d bestaat niet", copyId));
        }
        Copy copy = opionalCopy.get();

        if (!copy.isAvailable()) {
            throw new IllegalStateException("Exemplaar is al uitgeleend");
        }

        copy.setAvailable(false);
        copyRepository.save(copy);
    }

    public void returnCopy(Long copyId) {
        Optional<Copy> optionalCopy = copyRepository.findById(copyId);
        if (optionalCopy.isEmpty()) {
            throw new IllegalArgumentException(
                    "Exemplaar met id " + copyId + " bestaat niet.");
        }
        Copy copy = optionalCopy.get();
        if (copy.isAvailable()) {
            throw new IllegalStateException(
                    "Exemplaar is niet uitgeleend.");
        }
        copy.setAvailable(true);
        copyRepository.save(copy);
    }
}
