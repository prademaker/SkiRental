package nl.miwnn.ch19.paul.skirental.repository;
import nl.miwnn.ch19.paul.skirental.model.RentalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * @author Paul Rademaker
 * ---- VERVANG MIJ ----
 */
public interface UserRepository extends JpaRepository<RentalUser, Long> {
    Optional<RentalUser> findByUsername(String username);
}
