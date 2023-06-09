package io.github.xnovo3000.eventus.exception;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class EventusErrorController implements ErrorController {

    @GetMapping
    public String get(Model model) {
        // Set model for exception HTML and render it
        model.addAttribute("header", "500 - Internal server error");
        model.addAttribute("content", "Errore dell'applicazione, contattare l'amministratore");
        model.addAttribute("error", "Errore sconosciuto");
        return "page/exception";
    }

}