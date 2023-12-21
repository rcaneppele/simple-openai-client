package br.com.rcaneppele.openai.chatcompletion.request;

import br.com.rcaneppele.openai.common.message.ChatMessage;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ChatCompletionRequest(
        String model,
        Integer n,
        @JsonProperty("max_tokens")
        Integer maxTokens,
        @JsonProperty("frequency_penalty")
        Double frequencyPenalty,
        @JsonProperty("presence_penalty")
        Double presencePenalty,
        Double temperature,
        @JsonProperty("top_p")
        Double topP,
        String[] stop,
        Boolean stream,
        List<ChatMessage> messages
) {
}
