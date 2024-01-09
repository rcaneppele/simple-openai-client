package br.com.rcaneppele.openai.endpoints.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MessageContent(
        String type,
        Text text,
        @JsonProperty("image_file")
        Image imageFile
) {}
