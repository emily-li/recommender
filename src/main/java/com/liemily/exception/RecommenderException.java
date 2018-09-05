package com.liemily.exception;

public class RecommenderException extends Exception {
    public RecommenderException(String message) {
        super(message);
    }

    public RecommenderException(String message, Throwable cause) {
        super(message, cause);
    }
}
