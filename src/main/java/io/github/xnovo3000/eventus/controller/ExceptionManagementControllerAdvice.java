package io.github.xnovo3000.eventus.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionManagementControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolationException(ConstraintViolationException exception, Model model) {
        return handleInternal(model, "400: Bad Request", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleException(Exception exception, Model model) {
        return handleInternal(model, "500: Internal Server Error", exception.getMessage());
    }

    private String handleInternal(Model model, String title, String description) {
        // Set attributes
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        // Render HTML
        return "error";
    }

}