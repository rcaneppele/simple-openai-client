package br.com.rcaneppele.openai.common.response;

public record Error(
    ErrorType code,
    String message
) {}
