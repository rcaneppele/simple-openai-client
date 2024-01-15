package br.com.rcaneppele.openai.endpoints.run.response;

public record LastError(
    LastErrorCode code,
    String message
) {}
