package nl.miwnn.ch19.paul.skirental.controller;

import jakarta.validation.Valid;
import nl.miwnn.ch19.paul.skirental.model.Snowboard;
import nl.miwnn.ch19.paul.skirental.service.FileStorageService;
import nl.miwnn.ch19.paul.skirental.service.SnowboardService;
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

@Controller
@RequestMapping("/snowboard")
public class SnowboardController {
    private static final Logger log = LoggerFactory.getLogger(SnowboardController.class);

    private final SnowboardService snowboardService;
    private final FileStorageService fileStorageService;

    public SnowboardController(SnowboardService snowboardService, FileStorageService fileStorageService) {
        this.snowboardService = snowboardService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/all")
    public String showOverview(@RequestParam(required = false) String query, Model model) {
        model.addAttribute("snowboards", snowboardService.getSnowboards(query));
        model.addAttribute("paginaTitel", "Snowboard overzicht");
        model.addAttribute("activePage", "snowboards");
        model.addAttribute("query", query);
        return "snowboard";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("snowboard", new Snowboard());
        model.addAttribute("alleTypes", snowboardService.getAllTypes());
        model.addAttribute("activePage", "snowboards");
        return "add-edit-snowboard";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return snowboardService.findById(id).map(snowboard -> {
            model.addAttribute("snowboard", snowboard);
            model.addAttribute("alleTypes", snowboardService.getAllTypes());
            model.addAttribute("activePage", "snowboards");
            return "add-edit-snowboard";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("errorMessage", "Snowboard niet gevonden.");
            return "redirect:/snowboard/all";
        });
    }

    @PostMapping("/save")
    public String saveSnowboard(
            @Valid @ModelAttribute("snowboard") Snowboard snowboard,
            BindingResult bindingResult,
            @RequestParam("imageFile") MultipartFile imageFile, // Vang het bestand op
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("alleTypes", snowboardService.getAllTypes());
            return "add-edit-snowboard";
        }

        // 1. AFHANDELING VAN HET BESTAND
        if (!imageFile.isEmpty()) {
            try {
                // Sla het bestand op en krijg de unieke bestandsnaam terug
                String filename = fileStorageService.save(imageFile);

                // Sla het pad op in de entiteit (bijv. "/uploads/unieke-naam.jpg")
                // Dit pad komt overeen met wat we in WebConfig hebben ingesteld
                snowboard.setImageUrl("/uploads/" + filename);

            } catch (Exception e) {
                log.error("Fout bij opslaan van afbeelding: " + e.getMessage());
                // Optioneel: voeg een foutmelding toe voor de gebruiker
            }
        }

        // 2. DUPLICAAT CHECK
        if (snowboardService.isDuplicate(snowboard)) {
            bindingResult.rejectValue("model", "duplicate", "Deze combinatie van merk en model bestaat al.");
            model.addAttribute("alleTypes", snowboardService.getAllTypes());
            return "add-edit-snowboard";
        }

        // 3. OPSLAAN
        snowboardService.save(snowboard);
        redirectAttributes.addFlashAttribute("successMessage", "Snowboard succesvol opgeslagen!");
        return "redirect:/snowboard/all";
    }

    @PostMapping("/delete/{id}")
    public String deleteSnowboard(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        snowboardService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Snowboard succesvol verwijderd!");
        return "redirect:/snowboard/all";
    }

    @GetMapping("/{id}")
    public String snowboardDetail(@PathVariable Long id, Model model) {
        Snowboard snowboard = snowboardService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("snowboard", snowboard);
        model.addAttribute("activePage", "snowboards");
        return "snowboard-detail";
    }
}