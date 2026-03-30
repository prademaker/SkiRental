package nl.miwnn.ch19.paul.skirental.controller;

import jakarta.validation.Valid;
import nl.miwnn.ch19.paul.skirental.model.Snowboard;
import nl.miwnn.ch19.paul.skirental.service.SnowboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/snowboard")
public class SnowboardController {
    private static final Logger log = LoggerFactory.getLogger(SnowboardController.class);

    private final SnowboardService snowboardService;

    public SnowboardController(SnowboardService snowboardService) {
        this.snowboardService = snowboardService;
    }

    @GetMapping("/all")
    public String showOverview(Model model) {
        model.addAttribute("snowboards", snowboardService.findAll());
        model.addAttribute("paginaTitel", "Snowboard overzicht");
        model.addAttribute("activePage", "snowboards");
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
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("alleTypes", snowboardService.getAllTypes());
            return "add-edit-snowboard";
        }

        if (snowboardService.isDuplicate(snowboard)) {
            bindingResult.rejectValue("model", "duplicate", "Deze combinatie van merk en model bestaat al.");
            model.addAttribute("alleTypes", snowboardService.getAllTypes());
            return "add-edit-snowboard";
        }

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