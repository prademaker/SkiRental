package nl.miwnn.ch19.paul.skirental.controller;

import nl.miwnn.ch19.paul.skirental.model.Copy;
import nl.miwnn.ch19.paul.skirental.repository.CopyRepository;
import nl.miwnn.ch19.paul.skirental.repository.SkiRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * @author Paul Rademaker
 * Handle al requests regarding copies
 */

@Controller
@RequestMapping("/copies/")
public class CopyController {

    private static final Logger log = LoggerFactory.getLogger(CopyController.class);
    private final CopyRepository copyRepository;
    private final SkiRepository skiRepository;

    public CopyController(CopyRepository copyRepository, SkiRepository skiRepository) {
        this.copyRepository = copyRepository;
        this.skiRepository = skiRepository;
    }

    @PostMapping("/borrow/{id}")
    public String borrowCopy(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Copy> optionalCopy = copyRepository.findById(id);

        if (optionalCopy.isPresent() && optionalCopy.get().isAvailable()) {
            Copy copy = optionalCopy.get();
            copy.setAvailable(false);
            copyRepository.save(copy);
            log.info("Copy {} uitgeleend", id);
            redirectAttributes.addFlashAttribute("successMessage", "Ski uitgeleend!");
            return "redirect:/ski/" + copy.getSki().getId();
        }

        return "redirect:/ski/all";
    }

    @PostMapping("/return/{id}")
    public String returnCopy(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Copy> optionalCopy = copyRepository.findById(id);

        if (optionalCopy.isPresent() && !optionalCopy.get().isAvailable()) {
            Copy copy = optionalCopy.get();
            copy.setAvailable(true);
            copyRepository.save(copy);
            log.info("Copy {} teruggebracht", id);
            redirectAttributes.addFlashAttribute("successMessage", "Ski teruggebracht!");
            return "redirect:/ski/" + copy.getSki().getId();
        }

        return "redirect:/ski/all";
    }

    @PostMapping("/add/{skiId}")
    public String addCopy(@PathVariable Long skiId, RedirectAttributes redirectAttributes) {
        skiRepository.findById(skiId).ifPresent(ski -> {
            Copy copy = new Copy(ski);
            copyRepository.save(copy);
            log.info("Nieuw exemplaar toegevoegd aan ski {}", skiId);
        });
        redirectAttributes.addFlashAttribute("successMessage", "Exemplaar toegevoegd!");
        return "redirect:/ski/" + skiId;
    }

    @PostMapping("/delete/{id}")
    public String deleteCopy(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        copyRepository.findById(id).ifPresent(copy -> {
            Long skiId = copy.getSki().getId();
            copyRepository.deleteById(id);
            log.info("Exemplaar {} verwijderd", id);
            redirectAttributes.addFlashAttribute("successMessage", "Exemplaar verwijderd!");
        });

        return "redirect:/ski/all";
    }
}
