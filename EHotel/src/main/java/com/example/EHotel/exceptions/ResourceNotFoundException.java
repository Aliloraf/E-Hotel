package com.example.EHotel.exceptions;

/** Thrown when a requested record does not exist. Rendered as a 404 page. */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
