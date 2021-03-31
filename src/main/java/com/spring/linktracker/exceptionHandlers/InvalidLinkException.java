package com.spring.linktracker.exceptionHandlers;

public class InvalidLinkException extends Exception {

    public InvalidLinkException(String message) {
        super(message);
    }
}
