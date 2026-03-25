package nl.miwnn.ch19.paul.skirental.controller;

import jakarta.validation.Valid;
import nl.miwnn.ch19.paul.skirental.model.Ski;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Paul Rademaker
 * ---- VERVANG MIJ ----
 */


@Controller
public class SkiController {
    private final List<Ski> skis = new ArrayList<>();
    private static final Logger log =
            LoggerFactory.getLogger(SkiController.class);

    public SkiController() {
        skis.add(new Ski("Nordica", "Enforcer 99", "All-mountain"));
        skis.add(new Ski("K2", "Mindbender 89TI", "All-mountain"));
        skis.add(new Ski("Fischer", "Ranger 90", "All-mountain"));
        skis.add(new Ski("Atomic", "Bent 90", "All-mountain"));
        skis.add(new Ski("Black Crows", "Camox", "All-mountain"));
    }

    @GetMapping("/ski")
    public String showSkiOverview(
        @RequestParam(required = false) String query,
        Model model) {
            List<Ski> displaySkis;
        if (query != null && !query.isBlank()) {
            log.debug("Zoeken op query: {}", query);
            displaySkis = skis.stream()
                    .filter(book -> book.getMerk()
                            .toLowerCase()
                            .contains(query.toLowerCase()))
                    .toList();
        } else {
            displaySkis = skis;
        }
        log.debug("Skioverzicht opgevraagd, {} skis beschikbaar",
                skis.size());
        model.addAttribute("paginaTitel", "Ski overzicht");
        model.addAttribute("verhuurNaam", "Rent-a-ski Sappemeer");
        model.addAttribute("skis", displaySkis);
        return "ski";
    }

    @GetMapping("/ski/add")
    public String showAddBookForm(Model model) {
        log.debug("Formulier voor nieuwe ski opgevraagd");
        model.addAttribute("ski", new Ski());
        return "add-edit-ski";
    }

    @GetMapping("/ski/edit/{merk}")
    public String showEditForm(@PathVariable (required = false) String merk, Model model) {
        log.info("Bewerkformulier geopend voor: {}", merk);
        Ski skiToEdit = skis.stream()
                .filter(ski -> ski.getMerk().equals(merk))
                .findFirst()
                .orElse(null);
        if (skiToEdit == null) {
            log.warn("Ski niet gevonden voor bewerken: {}", merk);
            return "redirect:/ski";
        }
        model.addAttribute("ski", skiToEdit);
        return "add-edit-ski";
    }

    @GetMapping("/ski/delete/{merk}")
    public String deleteSki(@PathVariable @ModelAttribute String merk, RedirectAttributes redirectAttributes) {
        log.info("Verwijderverzoek ontvangen voor ski: {}", merk);
        skis.removeIf(ski -> ski.getMerk().equals(merk));
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

        for (int i = 0; i < skis.size(); i++) {
            if (skis.get(i).getMerk().equals(ski.getMerk())) {
                skis.set(i, ski);
                return "redirect:/ski";
            }
        }
        skis.add(ski);
        redirectAttributes.addFlashAttribute("successMessage", "Ski succesvol toegevoegd!");
        return "redirect:/ski";
    }
}
