package com.tierraburritoservidor.errors;

public class ApiError {

    private final String message;

    public ApiError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
