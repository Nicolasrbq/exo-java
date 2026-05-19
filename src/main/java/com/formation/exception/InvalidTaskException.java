package com.formation.exception;

public class InvalidTaskException extends RuntimeException {
    private final String field;

    public InvalidTaskException(String field, String reason) {
        super("Champ invalide '" + field + "' : " + reason);
        this.field = field;
    }

    public String getField() { return field; }
}
