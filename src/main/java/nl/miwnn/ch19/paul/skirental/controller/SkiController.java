package nl.miwnn.ch19.paul.skirental.controller;

import jakarta.validation.Valid;
import nl.miwnn.ch19.paul.skirental.model.Ski;
import nl.miwnn.ch19.paul.skirental.repository.SkiRepository;
import nl.miwnn.ch19.paul.skirental.repository.TypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * @author Paul Rademaker
 * ---- VERVANG MIJ ----
 */


@Controller
@RequestMapping("/ski")
public class SkiController {
    private static final Logger log = LoggerFactory.getLogger(SkiController.class);
    private final SkiRepository skiRepository;
    private final TypeRepository typeRepository;

    public SkiController(SkiRepository skiRepository, TypeRepository typeRepository) {
    this.skiRepository = skiRepository;
        this.typeRepository = typeRepository;
    }

    @GetMapping("/all")
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
        model.addAttribute("activePage", "skis");
        return "ski";
    }

    @GetMapping("/add")
    public String showAddSkiForm(Model model) {
        log.debug("Formulier voor nieuwe ski opgevraagd");
        model.addAttribute("ski", new Ski());
        model.addAttribute("alleTypes", typeRepository.findAll());
        return "add-edit-ski";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        log.info("Bewerkformulier geopend voor: {}", id);

        Optional<Ski> skiToEdit = skiRepository.findById(id);

        if (skiToEdit.isEmpty()) {
            log.warn("Ski met id: {} is niet gevonden voor bewerking", id);
            redirectAttributes.addFlashAttribute("skiNotFoundForEditing",
                    String.format("De ski met id: %d kon niet gevonden worden om te bewerken.", id));
            return "redirect:/ski/all";
        }

        model.addAttribute("ski", skiToEdit.get());
        model.addAttribute("alleTypes", typeRepository.findAll());
        return "add-edit-ski";
    }

    @PostMapping("/delete/{id}")
    public String deleteSki(@PathVariable @ModelAttribute Long id, RedirectAttributes redirectAttributes) {
        log.info("Verwijderverzoek ontvangen voor ski: {}", id);
        skiRepository.deleteById(id);
        redirectAttributes.addFlashAttribute(
                "successMessage", "Ski succesvol verwijderd!");
        return "redirect:/ski/all";
    }

    @PostMapping("/save")
    public String saveSki(
            @Valid @ModelAttribute("ski") Ski ski,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "add-edit-ski";
        }

        Optional<Ski> bestaande = skiRepository.findByMerkAndModel(ski.getMerk(), ski.getModel());
        if (bestaande.isPresent() && !bestaande.get().getId().equals(ski.getId())) {
            bindingResult.rejectValue("model", "duplicate", "Deze combinatie van merk en model bestaat al.");
            model.addAttribute("alleTypes", typeRepository.findAll());
            return "add-edit-ski";
        }

        skiRepository.save(ski);
        redirectAttributes.addFlashAttribute("successMessage", "Ski succesvol opgeslagen!");
        return "redirect:/ski/all";
    }

    @GetMapping("/{id}")
    public String skiDetail(@PathVariable Long id, Model model) {
        Ski ski = skiRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("ski", ski);
        return "ski-detail";
    }
}
