package io.github.xnovo3000.eventus.controller.error;

import jakarta.servlet.RequestDispatcher;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/error")
@Slf4j
public class EventusErrorController implements ErrorController {

    @GetMapping
    public String get(
            Model model,
            @RequestAttribute(RequestDispatcher.ERROR_STATUS_CODE) Integer statusCode
    ) {
        log.info("get called");
        // Get HTTP status code that must be thrown
        val status = Optional.ofNullable(HttpStatus.resolve(statusCode))
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
        // Set model for exception
        model.addAttribute("header", status.value() + " - " + status.getReasonPhrase());
        if (status == HttpStatus.FORBIDDEN) {
            model.addAttribute("content", "Non hai i permessi per accedere a questa pagina");
        } else {
            model.addAttribute("content", "Errore dell'applicazione, contattare l'amministratore");
        }
        // Render HTML
        return "page/exception";
    }

}