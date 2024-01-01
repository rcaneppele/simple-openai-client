package br.com.rcaneppele.openai.error.exception;

public class ServiceUnavailableException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

    public ServiceUnavailableException(String message) {
        super(message);
    }

}
