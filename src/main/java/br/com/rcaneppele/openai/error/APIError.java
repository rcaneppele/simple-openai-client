package br.com.rcaneppele.openai.error;

public record APIError(String message, String type, String code) {}
