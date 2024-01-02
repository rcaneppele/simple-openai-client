package br.com.rcaneppele.openai.endpoints.assistant.response;

public record DeletionStatus(
        String id,
        String object,
        boolean deleted
) {}
