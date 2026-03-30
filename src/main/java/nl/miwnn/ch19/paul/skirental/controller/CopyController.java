package nl.miwnn.ch19.paul.skirental.controller;

import nl.miwnn.ch19.paul.skirental.model.Copy;
import nl.miwnn.ch19.paul.skirental.repository.CopyRepository;
import nl.miwnn.ch19.paul.skirental.repository.SkiRepository;
import nl.miwnn.ch19.paul.skirental.repository.SnowboardRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/copies")
public class CopyController {

    private final CopyRepository copyRepository;
    private final SkiRepository skiRepository;
    private final SnowboardRepository snowboardRepository;

    public CopyController(CopyRepository copyRepository,
                          SkiRepository skiRepository,
                          SnowboardRepository snowboardRepository) {
        this.copyRepository = copyRepository;
        this.skiRepository = skiRepository;
        this.snowboardRepository = snowboardRepository;
    }

    @PostMapping("/borrow/{id}")
    public String borrowCopy(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Copy> optionalCopy = copyRepository.findById(id);

        if (optionalCopy.isPresent() && optionalCopy.get().isAvailable()) {
            Copy copy = optionalCopy.get();
            copy.setAvailable(false);
            copyRepository.save(copy);
            redirectAttributes.addFlashAttribute("successMessage", "Item uitgeleend!");
            return getRedirectUrl(copy);
        }
        return "redirect:/";
    }

    @PostMapping("/return/{id}")
    public String returnCopy(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Copy> optionalCopy = copyRepository.findById(id);

        if (optionalCopy.isPresent() && !optionalCopy.get().isAvailable()) {
            Copy copy = optionalCopy.get();
            copy.setAvailable(true);
            copyRepository.save(copy);
            redirectAttributes.addFlashAttribute("successMessage", "Item teruggebracht!");
            return getRedirectUrl(copy);
        }
        return "redirect:/";
    }

    @PostMapping("/add/{skiId}")
    public String addSkiCopy(@PathVariable Long skiId, RedirectAttributes redirectAttributes) {
        skiRepository.findById(skiId).ifPresent(ski -> {
            copyRepository.save(new Copy(ski));
        });
        redirectAttributes.addFlashAttribute("successMessage", "Ski exemplaar toegevoegd!");
        return "redirect:/ski/" + skiId;
    }

    @PostMapping("/add-snowboard/{snowboardId}")
    public String addSnowboardCopy(@PathVariable Long snowboardId, RedirectAttributes redirectAttributes) {
        snowboardRepository.findById(snowboardId).ifPresent(snowboard -> {
            copyRepository.save(new Copy(snowboard));});
        redirectAttributes.addFlashAttribute(
                "successMessage", "Snowboard exemplaar toegevoegd!");
        return "redirect:/snowboard/" + snowboardId;
    }

    @PostMapping("/delete/{id}")
    public String deleteCopy(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Copy> optionalCopy = copyRepository.findById(id);
        if (optionalCopy.isPresent()) {
            Copy copy = optionalCopy.get();
            String redirectUrl = getRedirectUrl(copy);
            copyRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Exemplaar verwijderd!");
            return redirectUrl;
        }
        return "redirect:/";
    }

    private String getRedirectUrl(Copy copy) {
        if (copy.getSki() != null) {
            return "redirect:/ski/" + copy.getSki().getId();
        } else if (copy.getSnowboard() != null) {
            return "redirect:/snowboard/" + copy.getSnowboard().getId();
        }
        return "redirect:/";
    }
}