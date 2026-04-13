package nl.miwnn.ch19.paul.skirental.controller;

import nl.miwnn.ch19.paul.skirental.model.Copy;
import nl.miwnn.ch19.paul.skirental.model.Rental;
import nl.miwnn.ch19.paul.skirental.repository.CopyRepository;
import nl.miwnn.ch19.paul.skirental.repository.CustomerRepository;
import nl.miwnn.ch19.paul.skirental.service.RentalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

/**
 * @author Paul Rademaker
 */

@Controller
@RequestMapping("/rental")
public class RentalController {
    private final RentalService rentalService;
    private final CustomerRepository customerRepository;
    private final CopyRepository copyRepository;

    public RentalController(RentalService rentalService,
                            CustomerRepository customerRepository,
                            CopyRepository copyRepository) {
        this.rentalService = rentalService;
        this.customerRepository = customerRepository;
        this.copyRepository = copyRepository;
    }

    @GetMapping("/add/{copyId}")
    public String showAddRentalForm(@PathVariable Long copyId, Model model) {
        Copy copy = copyRepository.findById(copyId).orElseThrow();

        Rental rental = new Rental();
        rental.setCopy(copy);
        rental.setStartDate(LocalDate.now());
        rental.setEndDate(LocalDate.now().plusDays(7));

        model.addAttribute("rental", rental);
        model.addAttribute("customers", customerRepository.findAll());
        model.addAttribute("copy", copy);

        return "rentals/add-rental";
    }

    @PostMapping("/add")
    public String addRental(@ModelAttribute Rental rental, RedirectAttributes redirectAttributes) {
        Rental savedRental = rentalService.createRental(rental);

        redirectAttributes.addFlashAttribute("successMessage",
                "Verhuur succesvol! Totaalprijs: €" +
                        String.format("%.2f", savedRental.getTotalPrice()));

        return "redirect:/ski/all";

    }
}

