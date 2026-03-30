package nl.miwnn.ch19.paul.skirental.controller;

import nl.miwnn.ch19.paul.skirental.model.Copy;
import nl.miwnn.ch19.paul.skirental.service.CopyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/copies")
public class CopyController {

    private final CopyService copyService;

    public CopyController(CopyService copyService) {
        this.copyService = copyService;
    }

    @PostMapping("/borrow/{id}")
    public String borrowCopy(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Copy copy = copyService.borrowCopy(id);
            redirectAttributes.addFlashAttribute("successMessage", "Item uitgeleend!");
            return getRedirectUrl(copy);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/return/{id}")
    public String returnCopy(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Copy copy = copyService.returnCopy(id);
            redirectAttributes.addFlashAttribute("successMessage", "Item teruggebracht!");
            return getRedirectUrl(copy);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/add/{skiId}")
    public String addSkiCopy(@PathVariable Long skiId, RedirectAttributes redirectAttributes) {
        copyService.addSkiCopy(skiId);
        redirectAttributes.addFlashAttribute("successMessage", "Ski exemplaar toegevoegd!");
        return "redirect:/ski/" + skiId;
    }

    @PostMapping("/add-snowboard/{snowboardId}")
    public String addSnowboardCopy(@PathVariable Long snowboardId, RedirectAttributes redirectAttributes) {
        copyService.addSnowboardCopy(snowboardId);
        redirectAttributes.addFlashAttribute("successMessage", "Snowboard exemplaar toegevoegd!");
        return "redirect:/snowboard/" + snowboardId;
    }

    @PostMapping("/delete/{id}")
    public String deleteCopy(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // We halen de copy eerst even op om te weten waar we naartoe moeten redirecten na verwijderen
        return copyService.findById(id).map(copy -> {
            String redirectUrl = getRedirectUrl(copy);
            copyService.deleteCopy(id);
            redirectAttributes.addFlashAttribute("successMessage", "Exemplaar verwijderd!");
            return redirectUrl;
        }).orElse("redirect:/");
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