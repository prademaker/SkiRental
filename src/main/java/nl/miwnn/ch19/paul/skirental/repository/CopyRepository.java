package nl.miwnn.ch19.paul.skirental.repository;

import nl.miwnn.ch19.paul.skirental.model.Copy;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Paul Rademaker
 */
public interface CopyRepository extends JpaRepository<Copy, Long> {
}
