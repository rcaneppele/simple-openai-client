package br.com.rcaneppele.openai.chatcompletion.response;

import br.com.rcaneppele.openai.common.TokenUsage;
import br.com.rcaneppele.openai.common.message.ChatCompletionChoice;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ChatCompletionResponse(
        String id,
        String object,
        Long created,
        String model,
        @JsonProperty("system_fingerprint")
        String systemFingerprint,
        List<ChatCompletionChoice> choices,
        TokenUsage usage
) {
}
