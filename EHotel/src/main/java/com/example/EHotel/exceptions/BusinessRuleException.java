package com.example.EHotel.exceptions;

/** Thrown when a request is well-formed but violates a business rule (e.g. a double booking). */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
