package nl.miwnn.ch19.paul.skirental.controller;

import jakarta.validation.Valid;
import nl.miwnn.ch19.paul.skirental.model.Ski;
import nl.miwnn.ch19.paul.skirental.repository.SkiRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * @author Paul Rademaker
 * ---- VERVANG MIJ ----
 */


@Controller
public class SkiController {
    private static final Logger log = LoggerFactory.getLogger(SkiController.class);
    private final SkiRepository skiRepository;

    public SkiController(SkiRepository skiRepository) {
    this.skiRepository = skiRepository;
    }


    @GetMapping("/ski")
    public String showSkiOverview(
        @RequestParam(required = false) String query,
        Model model) {
            List<Ski> skis;

        if (query != null && !query.isBlank()) {
            skis = skiRepository.findAll().stream()
                    .filter(ski -> ski.getMerk()
                            .toLowerCase()
                            .contains(query.toLowerCase()))
                    .toList();
        } else {
            skis = skiRepository.findAll();
        }
        log.debug("Skioverzicht opgevraagd, {} skis beschikbaar",
                skis.size());
        model.addAttribute("paginaTitel", "Ski overzicht");
        model.addAttribute("verhuurNaam", "Rent-a-ski Sappemeer");

        model.addAttribute("skis", skis);
        model.addAttribute("query", query);
        return "ski";
    }

    @GetMapping("/ski/add")
    public String showAddSkiForm(Model model) {
        log.debug("Formulier voor nieuwe ski opgevraagd");
        model.addAttribute("ski", new Ski());
        return "add-edit-ski";
    }

    @GetMapping("/ski/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        log.info("Bewerkformulier geopend voor: {}", id);

        Optional<Ski> skiToEdit = skiRepository.findById(id);

        if (skiToEdit.isEmpty()) {
            log.warn("Ski met id: {} is niet gevonden voor bewerking", id);
            redirectAttributes.addFlashAttribute("skiNotFoundForEditing",
                    String.format("De ski met id: %d kon niet gevonden worden om te bewerken.", id));
            return "redirect:/ski";
        }

        model.addAttribute("ski", skiToEdit);
        return "add-edit-ski";
    }


    @GetMapping("/ski/delete/{id}")
    public String deleteSki(@PathVariable @ModelAttribute Long id, RedirectAttributes redirectAttributes) {
        log.info("Verwijderverzoek ontvangen voor ski: {}", id);
        skiRepository.deleteById(id);
        redirectAttributes.addFlashAttribute(
                "successMessage", "Ski succesvol verwijderd!");
        return "redirect:/ski";
    }

    @PostMapping("/ski/save")
    public String saveSki(
            @Valid @ModelAttribute("ski") Ski ski,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            log.warn("Validatiefouten bij opslaan: {}", bindingResult.getErrorCount());
            return "add-edit-ski";
        }

       skiRepository.save(ski);
        log.info("Ski opgeslagen: {}", ski.getMerk());
        redirectAttributes.addFlashAttribute("successMessage", "Ski succesvol toegevoegd!");
        return "redirect:/ski";
    }
}
