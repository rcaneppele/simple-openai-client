package br.com.rcaneppele.openai.exception;

public class TimeoutException extends RuntimeException {

    public TimeoutException(String message) {
        super(message);
    }

}
