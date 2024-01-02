package br.com.rcaneppele.openai.endpoints.assistant.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ListOfAssistantFiles(
        String object,
        @JsonProperty("first_id")
        String firstId,
        @JsonProperty("last_id")
        String lastId,
        @JsonProperty("has_more")
        boolean hasMore,
        List<AssistantFile> data
) {}
