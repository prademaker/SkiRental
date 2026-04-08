package nl.miwnn.ch19.paul.skirental.repository;

import nl.miwnn.ch19.paul.skirental.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/**
 * @author Paul Rademaker
 * Repository voor het beheren van Customer data in de database
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Handig om een klant te zoeken op e-mailadres
    Optional<Customer> findByEmail(String email);

    // Handig voor een zoekfunctie: zoek op (een deel van) de achternaam
    List<Customer> findByLastNameContainingIgnoreCase(String lastName);

    // Zoek op volledige naam (handig voor duplicaten-check)
    List<Customer> findByFirstNameAndLastName(String firstName, String lastName);
}