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

import java.util.List;

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
        List<Snowboard> boards = snowboardService.getSnowboards(query);

        model.addAttribute("snowboards", boards);
        model.addAttribute("query", query);
        model.addAttribute("paginaTitel", "Snowboard Overzicht");
        model.addAttribute("activePage", "snowboards");

        // Nodig voor de modal: als er geen snowboard met fouten uit de flash-attributes komt, maak een nieuwe
        if (!model.containsAttribute("snowboard")) {
            model.addAttribute("snowboard", new Snowboard());
        }
        model.addAttribute("alleTypes", snowboardService.getAllTypes());

        return "snowboard";
    }

    @PostMapping("/save")
    public String saveSnowboard(@Valid @ModelAttribute("snowboard") Snowboard snowboard,
                                BindingResult bindingResult,
                                @RequestParam("imageFile") MultipartFile imageFile,
                                RedirectAttributes redirectAttributes) {

        // 1. Check op duplicaten via de service
        if (snowboardService.isDuplicate(snowboard)) {
            bindingResult.rejectValue("model", "duplicate", "Deze combinatie van merk en model bestaat al.");
        }

        if (bindingResult.hasErrors()) {
            // Sla de fouten op voor na de redirect zodat de modal weer opent
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.snowboard", bindingResult);
            redirectAttributes.addFlashAttribute("snowboard", snowboard);
            return "redirect:/snowboard/all";
        }

        // 2. Bestand opslaan als er een bestand is gekozen
        if (!imageFile.isEmpty()) {
            try {
                String filename = fileStorageService.save(imageFile);
                snowboard.setImageUrl("/uploads/" + filename);
            } catch (Exception e) {
                log.error("Fout bij uploaden snowboard afbeelding: " + e.getMessage());
            }
        }

        snowboardService.save(snowboard);
        redirectAttributes.addFlashAttribute("successMessage", "Snowboard succesvol opgeslagen!");
        return "redirect:/snowboard/all";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return snowboardService.findById(id).map(board -> {
            model.addAttribute("snowboard", board);
            model.addAttribute("alleTypes", snowboardService.getAllTypes());
            model.addAttribute("activePage", "snowboards");
            return "add-edit-snowboard"; // De aparte pagina voor bewerken
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("errorMessage", "Snowboard niet gevonden.");
            return "redirect:/snowboard/all";
        });
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