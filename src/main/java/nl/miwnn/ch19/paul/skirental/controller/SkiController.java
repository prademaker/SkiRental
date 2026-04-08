package nl.miwnn.ch19.paul.skirental.controller;

import jakarta.validation.Valid;
import nl.miwnn.ch19.paul.skirental.model.Ski;
import nl.miwnn.ch19.paul.skirental.service.FileStorageService;
import nl.miwnn.ch19.paul.skirental.service.SkiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * @author Paul Rademaker
 */


@Controller
@RequestMapping("/ski")
public class SkiController {
    private static final Logger log = LoggerFactory.getLogger(SkiController.class);

    private final SkiService skiService;
    private final FileStorageService fileStorageService;

    public SkiController(SkiService skiService, FileStorageService fileStorageService) {
        this.skiService = skiService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/all")
    public String showSkiOverview(@RequestParam(required = false) String query, Model model) {
        List<Ski> skis = skiService.getSkis(query);

        log.debug("Skioverzicht opgevraagd, {} skis gevonden", skis.size());

        model.addAttribute("paginaTitel", "Ski overzicht");
        model.addAttribute("verhuurNaam", "Rent-a-ski Sappemeer");
        model.addAttribute("skis", skis);
        model.addAttribute("query", query);
        model.addAttribute("activePage", "skis");
        return "ski";
    }

    @GetMapping("/add")
    public String showAddSkiForm(Model model) {
        model.addAttribute("ski", new Ski());
        model.addAttribute("alleTypes", skiService.getAllTypes());
        return "add-edit-ski";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Ski> skiToEdit = skiService.findById(id);

        if (skiToEdit.isEmpty()) {
            redirectAttributes.addFlashAttribute("skiNotFoundForEditing",
                    String.format("Ski %d niet gevonden", id));
            return "redirect:/ski/all";
        }

        model.addAttribute("ski", skiToEdit.get());
        model.addAttribute("alleTypes", skiService.getAllTypes());
        return "add-edit-ski";
    }

    @PostMapping("/save")
    public String saveSki(@Valid @ModelAttribute("ski") Ski ski,
                          BindingResult bindingResult,
                          @RequestParam("imageFile") MultipartFile imageFile, // Nieuw!
                          RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "add-edit-ski";
        }

        // Check of er een bestand is geüpload
        if (!imageFile.isEmpty()) {
            String filename = fileStorageService.save(imageFile);
            ski.setImageUrl("/uploads/" + filename); // Sla het virtuele pad op
        }

        skiService.save(ski);
        return "redirect:/ski/all";
    }

    @PostMapping("/delete/{id}")
    public String deleteSki(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        skiService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Ski succesvol verwijderd!");
        return "redirect:/ski/all";
    }

    @GetMapping("/{id}")
    public String skiDetail(@PathVariable Long id, Model model) {
        Ski ski = skiService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("ski", ski);
        return "ski-detail";
    }
}