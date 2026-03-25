package nl.miwnn.ch19.paul.skirental.repository;

import nl.miwnn.ch19.paul.skirental.model.Ski;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Paul Rademaker
 */
public interface SkiRepository extends JpaRepository<Ski, Long> {

}
