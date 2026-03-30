package nl.miwnn.ch19.paul.skirental.controller;

import jakarta.validation.Valid;
import nl.miwnn.ch19.paul.skirental.model.Snowboard;
import nl.miwnn.ch19.paul.skirental.repository.SnowboardRepository;
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

import java.util.Optional;

@Controller
@RequestMapping("/snowboard")
public class SnowboardController {
    private static final Logger log = LoggerFactory.getLogger(SnowboardController.class);
    private final SnowboardRepository snowboardRepository;
    private final TypeRepository typeRepository;

    public SnowboardController(SnowboardRepository snowboardRepository, TypeRepository typeRepository) {
        this.snowboardRepository = snowboardRepository;
        this.typeRepository = typeRepository;
    }

    @GetMapping("/all")
    public String showOverview(Model model) {
        model.addAttribute("snowboards", snowboardRepository.findAll());
        model.addAttribute("paginaTitel", "Snowboard overzicht");
        model.addAttribute("activePage", "snowboards");
        return "snowboard";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("snowboard", new Snowboard());
        model.addAttribute("alleTypes", typeRepository.findAll());
        model.addAttribute("activePage", "snowboards");
        return "add-edit-snowboard";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Snowboard> snowboardToEdit = snowboardRepository.findById(id);

        if (snowboardToEdit.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    String.format("Snowboard met id %d kon niet gevonden worden.", id));
            return "redirect:/snowboard/all";
        }

        model.addAttribute("snowboard", snowboardToEdit.get());
        model.addAttribute("alleTypes", typeRepository.findAll());
        model.addAttribute("activePage", "snowboards");
        return "add-edit-snowboard";
    }

    @PostMapping("/save")
    public String saveSnowboard(
            @Valid @ModelAttribute("snowboard") Snowboard snowboard,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("alleTypes", typeRepository.findAll());
            return "add-edit-snowboard";
        }

        Optional<Snowboard> bestaande = snowboardRepository.findByMerkAndModel(snowboard.getMerk(), snowboard.getModel());
        if (bestaande.isPresent() && !bestaande.get().getId().equals(snowboard.getId())) {
            bindingResult.rejectValue("model", "duplicate", "Deze combinatie van merk en model bestaat al.");
            model.addAttribute("alleTypes", typeRepository.findAll());
            return "add-edit-snowboard";
        }

        snowboardRepository.save(snowboard);
        redirectAttributes.addFlashAttribute("successMessage", "Snowboard succesvol opgeslagen!");
        return "redirect:/snowboard/all";
    }

    @GetMapping("/delete/{id}")
    public String deleteSnowboard(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        snowboardRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Snowboard succesvol verwijderd!");
        return "redirect:/snowboard/all";
    }

    @GetMapping("/{id}")
    public String snowboardDetail(@PathVariable Long id, Model model) {
        Snowboard snowboard = snowboardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("snowboard", snowboard);
        model.addAttribute("activePage", "snowboards");
        return "snowboard-detail";
    }
}