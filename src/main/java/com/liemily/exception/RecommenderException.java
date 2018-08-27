package com.liemily.exception;

public class RecommenderException extends Exception {
    public RecommenderException(String message) {
        super(message);
    }

    public RecommenderException(Throwable cause) {
        super(cause);
    }
}
