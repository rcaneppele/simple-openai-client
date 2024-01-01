package br.com.rcaneppele.openai.endpoints.assistant.request;

import br.com.rcaneppele.openai.endpoints.assistant.tools.Tool;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Set;

public record CreateAssistantRequest(
        String model,
        String name,
        String description,
        String instructions,
        Set<Tool> tools,
        @JsonProperty("file_ids")
        Set<String> fileIds,
        Map<String, String> metadata
) {}
