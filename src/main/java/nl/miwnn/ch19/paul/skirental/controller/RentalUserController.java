package nl.miwnn.ch19.paul.skirental.controller;

import jakarta.validation.Valid;
import nl.miwnn.ch19.paul.skirental.dto.NewRentalUserDTO;
import nl.miwnn.ch19.paul.skirental.mapper.RentalUserMapper;
import nl.miwnn.ch19.paul.skirental.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Paul Rademaker
 * ---- VERVANG MIJ ----
 */

@Controller
@RequestMapping("/users")
public class RentalUserController {

    private final UserRepository userRepository;
    private final RentalUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private static final Logger log =
            LoggerFactory.getLogger(RentalUserController.class);


    public RentalUserController(
            UserRepository userRepository,
            RentalUserMapper userMapper,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("")
    public String showUserOverview(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("newUser", new NewRentalUserDTO());
        model.addAttribute("activePage", "user");
        return "users/overview";
    }

    @GetMapping("/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("newUser", new NewRentalUserDTO());
        return "users/add-user";
    }

    @PostMapping("/add")
    public String addUser(@Valid @ModelAttribute("newUser") NewRentalUserDTO dto,
                          BindingResult bindingResult,
                          Model model,
                          RedirectAttributes redirectAttributes) {

        if (dto.getPlainPassword() != null && !dto.getPlainPassword().equals(dto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.passwordMismatch",
                    "De ingevulde wachtwoorden komen niet overeen.");
        }

        if (bindingResult.hasErrors()) {
            log.warn("Validatiefout bij aanmaken gebruiker: {}", bindingResult.getAllErrors());
            return "users/add-user";
        }

        try {
            userRepository.save(userMapper.toRentalUser(dto, passwordEncoder));
            redirectAttributes.addFlashAttribute("successMessage",
                    "Gebruiker '" + dto.getUsername() + "' is succesvol aangemaakt.");

        } catch (Exception e) {
            log.error("Fout bij opslaan gebruiker: ", e);
            model.addAttribute("errorMessage", "Er is een technische fout opgetreden bij het opslaan.");
            return "users/add-user";
        }
        return "redirect:/users";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        userRepository.deleteById(id);
        redirectAttributes.addFlashAttribute(
                "succesMessage", "Gebruiker verwijderd.");
        return "redirect:/users";
    }
}
