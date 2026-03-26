package nl.miwnn.ch19.paul.skirental.repository;

import nl.miwnn.ch19.paul.skirental.model.Ski;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Paul Rademaker
 */
public interface SkiRepository extends JpaRepository<Ski, Long> {

    Optional<Ski> findSkiByMerk(String merk);
    Optional<Ski> findByMerkAndModel(
            String merk, String model);
}

