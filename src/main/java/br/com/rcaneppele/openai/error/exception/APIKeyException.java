package br.com.rcaneppele.openai.error.exception;

public class APIKeyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public APIKeyException(String message) {
        super(message);
    }

}
