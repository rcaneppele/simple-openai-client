package br.com.rcaneppele.openai.endpoints.assistant.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record AssistantFile(
        String id,
        String object,
        @JsonProperty("created_at")
        Instant createdAt,
        @JsonProperty("assistant_id")
        String assistantId
) {}
