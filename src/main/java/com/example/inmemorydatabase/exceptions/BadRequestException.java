package com.example.inmemorydatabase.exceptions;

public class BadRequestException extends Exception {

    private final String message;

    public BadRequestException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
