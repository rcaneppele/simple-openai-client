package br.com.rcaneppele.openai.endpoints.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FileCitation(
        @JsonProperty("file_id")
        String fileId,
        String quote
) {}
