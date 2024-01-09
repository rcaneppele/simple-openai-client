package br.com.rcaneppele.openai.endpoints.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Set;

public record CreateMessageRequest(
        String threadId,
        String role,
        String content,
        @JsonProperty("file_ids")
        Set<String> fileIds,
        Map<String, String> metadata
) {}
