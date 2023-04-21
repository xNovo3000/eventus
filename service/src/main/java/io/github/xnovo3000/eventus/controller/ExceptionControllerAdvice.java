package io.github.xnovo3000.eventus.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationError(Model model) {
        model.addAttribute("header", "400 - Bad Request");
        model.addAttribute("content", "La richiesta inviata non risulta valida. Se si ritiene sia un errore, contattare l'amministratore");
        return "page/exception";
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleInternalServerError(Model model) {
        model.addAttribute("header", "500 - Internal Server Error");
        model.addAttribute("content", "Errore dell'applicazione, contattare l'amministratore");
        return "page/exception";
    }

}