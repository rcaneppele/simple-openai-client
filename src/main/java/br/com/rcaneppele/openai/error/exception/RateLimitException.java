package br.com.rcaneppele.openai.error.exception;

public class RateLimitException extends RuntimeException {

    public RateLimitException(String message) {
        super(message);
    }

}
