package nl.miwnn.ch19.paul.skirental.repository;

import nl.miwnn.ch19.paul.skirental.model.Snowboard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SnowboardRepository extends JpaRepository<Snowboard, Long> {
    Optional<Snowboard> findByMerkAndModel(String merk, String model);
    Optional<Snowboard> findByMerkIgnoreCaseAndModelIgnoreCase(String merk, String model);
    List<Snowboard> findByMerkContainingIgnoreCase(String merk);
}