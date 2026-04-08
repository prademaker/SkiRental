package nl.miwnn.ch19.paul.skirental.service;

import jakarta.transaction.Transactional;
import nl.miwnn.ch19.paul.skirental.model.Copy;
import nl.miwnn.ch19.paul.skirental.model.Rental;
import nl.miwnn.ch19.paul.skirental.repository.CopyRepository;
import nl.miwnn.ch19.paul.skirental.repository.RentalRepository;
import org.springframework.stereotype.Service;

/**
 * @author Paul Rademaker
 * ---- Programma dat dingen doet ----
 * ---- VERVANG MIJ ----
 */

@Service
public class RentalService {
    private final RentalRepository rentalRepository;
    private final CopyRepository copyRepository;

    public RentalService(RentalRepository rentalRepository, CopyRepository copyRepository) {
        this.rentalRepository = rentalRepository;
        this.copyRepository = copyRepository;
    }

    @Transactional
    public Rental createRental(Rental rental) {
        Copy copy = copyRepository.findById(rental.getCopy().getId()).orElseThrow();

        // 1. Prijs bepalen
        double dailyPrice = (copy.getSki() != null) ?
                copy.getSki().getDailyPrice() :
                copy.getSnowboard().getDailyPrice();

        // 2. Dagen berekenen
        long days = java.time.temporal.ChronoUnit.DAYS.between(rental.getStartDate(), rental.getEndDate());
        if (days <= 0) days = 1; // Minimum 1 dag

        rental.setTotalPrice(days * dailyPrice);

        // 3. Item op 'niet beschikbaar' zetten
        copy.setAvailable(false);
        copyRepository.save(copy);

        return rentalRepository.save(rental);
    }
}