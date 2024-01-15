package br.com.rcaneppele.openai.endpoints.run.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MessageCreation(
    @JsonProperty("message_id")
    String messageId
) {}
