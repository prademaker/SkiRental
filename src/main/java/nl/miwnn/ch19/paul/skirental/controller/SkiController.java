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

        model.addAttribute("skis", skis);
        model.addAttribute("query", query);
        model.addAttribute("paginaTitel", "Ski Overzicht");
        model.addAttribute("activePage", "skis");

        // Nodig voor de 'Nieuwe Ski' Modal op deze pagina
        if (!model.containsAttribute("ski")) {
            model.addAttribute("ski", new Ski());
        }
        model.addAttribute("alleTypes", skiService.getAllTypes());

        return "ski";
    }

    @PostMapping("/save")
    public String saveSki(@Valid @ModelAttribute("ski") Ski ski,
                          BindingResult bindingResult,
                          @RequestParam("imageFile") MultipartFile imageFile,
                          Model model,
                          RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            // Als er fouten zijn, sturen we de gebruiker terug naar de overzichtspagina
            // FlashAttributes zorgen dat de foutmeldingen en de ingevulde data bewaard blijven voor de modal
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.ski", bindingResult);
            redirectAttributes.addFlashAttribute("ski", ski);
            return "redirect:/ski/all";
        }

        // 1. Check op duplicaten
        if (skiService.isDuplicate(ski)) {
            bindingResult.rejectValue("model", "duplicate", "Deze combinatie van merk en model bestaat al.");
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.ski", bindingResult);
            redirectAttributes.addFlashAttribute("ski", ski);
            return "redirect:/ski/all";
        }

        // 2. Bestand opslaan
        if (!imageFile.isEmpty()) {
            try {
                String filename = fileStorageService.save(imageFile);
                ski.setImageUrl("/uploads/" + filename);
            } catch (Exception e) {
                log.error("Upload fout: " + e.getMessage());
            }
        }

        skiService.save(ski);
        redirectAttributes.addFlashAttribute("successMessage", "Ski succesvol opgeslagen!");
        return "redirect:/ski/all";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return skiService.findById(id).map(ski -> {
            model.addAttribute("ski", ski);
            model.addAttribute("alleTypes", skiService.getAllTypes());
            return "add-edit-ski"; // Hergebruik je bestaande edit-pagina
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("errorMessage", "Ski niet gevonden.");
            return "redirect:/ski/all";
        });
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