package nl.miwnn.ch19.paul.skirental.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

/**
 * @author Paul Rademaker
 * ---- VERVANG MIJ ----
 */

@Controller
public class IndexController {
    private static final Logger log =
            LoggerFactory.getLogger(IndexController.class);

    @GetMapping("/")
        public String showIndex (Model model){
            log.debug("De startpagina is geladen!");
            log.info("De startpagina is geladen om {}", LocalDate.now());
            model.addAttribute("naam", "Paul Rademaker");
            model.addAttribute("datum", LocalDate.now().toString());
        return "index";
    }
}