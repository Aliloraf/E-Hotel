package com.example.EHotel.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.example.EHotel.exceptions.BusinessRuleException;
import com.example.EHotel.exceptions.ResourceNotFoundException;

/** Turns application exceptions into a readable error page instead of a stack trace. */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** Treat blank form fields as null so that optional fields and @NotNull checks behave as expected. */
    @InitBinder
    public void trimStrings(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(ResourceNotFoundException ex, Model model) {
        return errorPage(model, HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(BusinessRuleException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleBusinessRule(BusinessRuleException ex, Model model) {
        return errorPage(model, HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDataIntegrity(DataIntegrityViolationException ex, Model model) {
        log.warn("Database constraint violation: {}", ex.getMostSpecificCause().getMessage());
        return errorPage(model, HttpStatus.CONFLICT,
                "L'opération a été refusée par la base de données : elle viole une contrainte d'intégrité "
                        + "(par exemple, l'enregistrement est encore utilisé par des réservations ou des locations).");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleTypeMismatch(MethodArgumentTypeMismatchException ex, Model model) {
        return errorPage(model, HttpStatus.BAD_REQUEST, "Paramètre invalide : " + ex.getName());
    }

    private String errorPage(Model model, HttpStatus status, String message) {
        model.addAttribute("status", status.value());
        model.addAttribute("error", status.getReasonPhrase());
        model.addAttribute("message", message);
        return "error";
    }
}
