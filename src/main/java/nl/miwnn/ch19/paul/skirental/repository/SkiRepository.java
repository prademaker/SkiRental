package nl.miwnn.ch19.paul.skirental.repository;

import model.Ski;

import nl.miwnn.ch19.paul.skirental.model.Ski;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Ski, Long> {
}