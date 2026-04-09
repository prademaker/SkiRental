package nl.miwnn.ch19.paul.skirental.controller;

import jakarta.validation.Valid;
import nl.miwnn.ch19.paul.skirental.model.Customer;
import nl.miwnn.ch19.paul.skirental.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/all")
    public String showCustomerOverview(Model model) {
        model.addAttribute("customers", customerService.findAll());
        model.addAttribute("paginaTitel", "Klantenbeheer");
        model.addAttribute("activePage", "customers");

        if (!model.containsAttribute("customer")) {
            model.addAttribute("customer", new Customer());
        }
        return "customer";
    }

    @PostMapping("/save")
    public String saveCustomer(@Valid @ModelAttribute("customer") Customer customer,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.customer", bindingResult);
            redirectAttributes.addFlashAttribute("customer", customer);
            return "redirect:/customer/all";
        }

        customerService.save(customer);
        redirectAttributes.addFlashAttribute("successMessage", "Klant succesvol opgeslagen!");
        return "redirect:/customer/all";
    }

    @PostMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        customerService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Klant verwijderd.");
        return "redirect:/customer/all";
    }
}