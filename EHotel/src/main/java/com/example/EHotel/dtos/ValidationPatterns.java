package com.example.EHotel.dtos;

/** Regular expressions shared by the form DTOs. They mirror the CHECK constraints in schema.sql. */
public final class ValidationPatterns {

    public static final String SIN = "^\\d{3}-\\d{3}-\\d{3}$";
    public static final String COUNTRY = "^(US|CA|MX)$";
    /** US ZIP, Mexican and Canadian postal codes (case-insensitive, like the database check). */
    public static final String POSTAL_CODE = "(?i)^(\\d{5}(-\\d{4})?|\\d{3}-\\d{3}|[ABCEGHJKLMNPRSTVXY]\\d[A-Z] *\\d[A-Z]\\d)$";
    public static final String EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public static final String SIN_MESSAGE = "Le NAS doit avoir le format 123-456-789";
    public static final String COUNTRY_MESSAGE = "Le pays doit être US, CA ou MX";
    public static final String POSTAL_CODE_MESSAGE = "Code postal invalide (ex. 10036, K1N 6N5 ou 06010)";
    public static final String EMAIL_MESSAGE = "Adresse courriel invalide";

    private ValidationPatterns() {
    }
}
