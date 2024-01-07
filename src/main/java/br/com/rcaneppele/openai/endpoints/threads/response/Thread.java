package br.com.rcaneppele.openai.endpoints.threads.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Map;

public record Thread(
        String id,
        String object,
        @JsonProperty("created_at")
        Instant createdAt,
        Map<String, String> metadata
) {}
