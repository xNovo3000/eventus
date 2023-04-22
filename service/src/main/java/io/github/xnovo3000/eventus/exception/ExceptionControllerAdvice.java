package io.github.xnovo3000.eventus.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationError(Model model, MethodArgumentNotValidException exception) {
        // Log exception anyway
        log.error("Application error", exception);
        // Set model for exception HTML and render it
        model.addAttribute("header", "400 - Bad Request");
        model.addAttribute("content", "La richiesta inviata non risulta valida. Se si ritiene sia un errore, contattare l'amministratore");
        model.addAttribute("error", exception.getMessage());
        return "page/exception";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(Model model) {
        // This is not an exception, logging not needed because something has not been found
        // Set model for exception HTML and render it
        model.addAttribute("header", "404 - Not Found");
        model.addAttribute("content", "La risorsa che stavi cercando non è stata trovata");
        model.addAttribute("error", "NotFound");
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
        model.addAttribute("error", exception.getMessage());
        return "page/exception";
    }

}