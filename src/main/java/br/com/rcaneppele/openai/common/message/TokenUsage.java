package br.com.rcaneppele.openai.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenUsage(
        @JsonProperty("prompt_tokens")
        Integer promptTokens,
        @JsonProperty("completion_tokens")
        Integer completionTokens,
        @JsonProperty("total_tokens")
        Integer totalTokens
) {
}
