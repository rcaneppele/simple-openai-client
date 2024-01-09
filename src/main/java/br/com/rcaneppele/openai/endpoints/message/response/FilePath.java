package br.com.rcaneppele.openai.endpoints.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FilePath(
        @JsonProperty("file_id")
        String fileId
) {}
