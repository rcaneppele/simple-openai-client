package br.com.rcaneppele.openai.endpoints.assistant.request;

public record ListAssistantsRequest(
        int limit,
        String order,
        String after,
        String before
) {}
