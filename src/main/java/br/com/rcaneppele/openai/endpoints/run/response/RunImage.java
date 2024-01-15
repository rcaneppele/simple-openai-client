package br.com.rcaneppele.openai.endpoints.run.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RunImage(
    @JsonProperty("file_id")
    String fileId
) {}
