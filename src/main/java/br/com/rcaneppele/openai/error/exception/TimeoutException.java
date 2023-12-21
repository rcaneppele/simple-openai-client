package br.com.rcaneppele.openai.error.exception;

public class TimeoutException extends RuntimeException {

    public TimeoutException(String message) {
        super(message);
    }

}
