package br.com.rcaneppele.openai.common.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChatCompletionChoice(
        Long index,
        ChatMessage message,
        @JsonProperty("logprobs")
        Object logProbs,
        @JsonProperty("finish_reason")
        String finishReason
) {
}
