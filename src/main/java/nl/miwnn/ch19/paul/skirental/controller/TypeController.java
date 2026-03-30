package nl.miwnn.ch19.paul.skirental.controller;

import jakarta.validation.Valid;
import nl.miwnn.ch19.paul.skirental.model.Type;
import nl.miwnn.ch19.paul.skirental.repository.TypeRepository;
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
    private final TypeRepository typeRepository;

    public TypeController(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    @GetMapping
    public String showOverview(Model model) {
        model.addAttribute("types", typeRepository.findAll());
        model.addAttribute("type", new Type());
        model.addAttribute("activePage", "type");
        //model.addAttribute("activePage", type);
        return "type";
    }

    @PostMapping("/save")
    public String saveType(@Valid @ModelAttribute Type type,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("types", typeRepository.findAll());
            return "type";
        }

        typeRepository.save(type);
        redirectAttributes.addFlashAttribute("successMessage", "Type opgeslagen!");
        return "redirect:/type";
    }

    @PostMapping("/delete/{id}")
    public String deleteType(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        typeRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Type verwijderd!");
        return "redirect:/type";
    }
}