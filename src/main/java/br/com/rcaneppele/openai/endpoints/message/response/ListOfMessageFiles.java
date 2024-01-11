package br.com.rcaneppele.openai.endpoints.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ListOfMessageFiles(
        String object,
        @JsonProperty("first_id")
        String firstId,
        @JsonProperty("last_id")
        String lastId,
        @JsonProperty("has_more")
        boolean hasMore,
        List<MessageFile> data
) {}
