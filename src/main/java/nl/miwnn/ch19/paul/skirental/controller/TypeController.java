package nl.miwnn.ch19.paul.skirental.controller;

import jakarta.validation.Valid;
import nl.miwnn.ch19.paul.skirental.model.Type;
import nl.miwnn.ch19.paul.skirental.service.TypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/type")
public class TypeController {

    private static final Logger log = LoggerFactory.getLogger(TypeController.class);
    private final TypeService typeService;

    // Injecteer alleen de TypeService
    public TypeController(TypeService typeService) {
        this.typeService = typeService;
    }

    @GetMapping
    public String showOverview(Model model) {
        model.addAttribute("types", typeService.findAll());
        model.addAttribute("type", new Type()); // Voor het 'add' formulier op dezelfde pagina
        model.addAttribute("activePage", "type");
        return "type";
    }

    @PostMapping("/save")
    public String saveType(@Valid @ModelAttribute Type type,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("types", typeService.findAll());
            return "type";
        }

        typeService.save(type);
        redirectAttributes.addFlashAttribute("successMessage", "Type succesvol opgeslagen!");
        return "redirect:/type";
    }

    @PostMapping("/delete/{id}")
    public String deleteType(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("Verzoek om type {} te verwijderen", id);
        try {
            typeService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Type verwijderd!");
        } catch (Exception e) {
            log.error("Fout bij verwijderen van type: ", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Type kon niet worden verwijderd (mogelijk nog in gebruik door een ski).");
        }
        return "redirect:/type";
    }
}