package br.com.rcaneppele.openai.endpoints.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record MessageFile(
        String id,
        String object,
        @JsonProperty("created_at")
        Instant createdAt,
        @JsonProperty("message_id")
        String messageId
) {}
