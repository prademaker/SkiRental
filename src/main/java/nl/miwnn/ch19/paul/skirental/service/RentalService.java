package nl.miwnn.ch19.paul.skirental.service;

import jakarta.transaction.Transactional;
import nl.miwnn.ch19.paul.skirental.model.Copy;
import nl.miwnn.ch19.paul.skirental.model.Rental;
import nl.miwnn.ch19.paul.skirental.repository.CopyRepository;
import nl.miwnn.ch19.paul.skirental.repository.RentalRepository;
import org.springframework.stereotype.Service;

/**
 * @author Paul Rademaker
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

        double dailyPrice = (copy.getSki() != null) ?
                copy.getSki().getDailyPrice() :
                copy.getSnowboard().getDailyPrice();

        long days = java.time.temporal.ChronoUnit.DAYS.between(rental.getStartDate(), rental.getEndDate());
        if (days <= 0) days = 1;

        rental.setTotalPrice(days * dailyPrice);

        copy.setAvailable(false);
        copyRepository.save(copy);

        return rentalRepository.save(rental);
    }
}