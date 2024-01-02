package br.com.rcaneppele.openai.endpoints.assistant.response;

import br.com.rcaneppele.openai.endpoints.assistant.tools.Tool;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

public record Assistant(
        String id,
        String object,
        @JsonProperty("created_at")
        Instant createdAt,
        String name,
        String description,
        String model,
        String instructions,
        Set<Tool> tools,
        @JsonProperty("file_ids")
        Set<String> fileIds,
        Map<String, String> metadata
) {}
