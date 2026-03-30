package nl.miwnn.ch19.paul.skirental.handler;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class SkiRentalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public String handleNotFound(
            ResponseStatusException exception,
            Model model) {
        model.addAttribute("statusCode", exception.getStatusCode().value());
        model.addAttribute("bericht", exception.getReason());
        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    public String handleAlgemeneUitzondering(
            Exception exception,
            Model model){
        model.addAttribute("bericht", exception.getMessage());
        return "error/500";
    }
}