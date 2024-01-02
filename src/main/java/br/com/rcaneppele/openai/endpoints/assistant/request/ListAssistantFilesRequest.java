package br.com.rcaneppele.openai.endpoints.assistant.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ListAssistantFilesRequest(
        @JsonProperty("assistant_id")
        String assistantId,
        int limit,
        String order,
        String after,
        String before
) implements ListQueryParameters {}
