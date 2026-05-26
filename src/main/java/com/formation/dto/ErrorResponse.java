package com.formation.dto;

import java.time.Instant;

public record ErrorResponse(
        String timestamp, // ISO-8601 : Instant.now().toString()
        int status, // code HTTP numérique
        String error, // code lisible : "TASK_NOT_FOUND"
        String message, // description humaine
        String path // URL appelée : "/tasks/42"
) {
    // Constructeur de commodité
    public ErrorResponse(int status, String error, String message, String path) {
        this(Instant.now().toString(), status, error, message, path);
    }
}
