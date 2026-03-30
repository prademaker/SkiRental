package nl.miwnn.ch19.paul.skirental.repository;

import nl.miwnn.ch19.paul.skirental.model.Snowboard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SnowboardRepository extends JpaRepository<Snowboard, Long> {
    Optional<Snowboard> findByMerkAndModel(String merk, String model);
}