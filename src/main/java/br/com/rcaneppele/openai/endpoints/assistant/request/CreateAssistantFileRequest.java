package br.com.rcaneppele.openai.endpoints.assistant.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateAssistantFileRequest(
        @JsonProperty("file_id")
        String fileId
) {}
