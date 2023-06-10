package io.github.xnovo3000.eventus.controller.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controller used in case of unhandled exception
 */
@ControllerAdvice
@Slf4j
public class EventusErrorControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationError(Model model, MethodArgumentNotValidException exception) {
        // Log exception anyway
        log.error("Application error", exception);
        // Set model for exception HTML and render it
        model.addAttribute("header", "400 - Bad Request");
        model.addAttribute("content", "La richiesta inviata non risulta valida");
        return "page/exception";
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleInternalServerError(Model model, Exception exception) {
        // Log exception anyway
        log.error("Application error", exception);
        // Set model for exception HTML and render it
        model.addAttribute("header", "500 - Internal Server Error");
        model.addAttribute("content", "Errore dell'applicazione, contattare l'amministratore");
        return "page/exception";
    }

}