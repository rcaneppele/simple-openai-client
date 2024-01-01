package br.com.rcaneppele.openai.endpoints.chatcompletion.request;

import br.com.rcaneppele.openai.common.message.ChatMessage;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

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
        List<ChatMessage> messages,
        Boolean logprobs,
        @JsonProperty("top_logprobs")
        Integer topLogprobs,
        String user,
        Integer seed,
        @JsonProperty("logit_bias")
        Map<Integer, Integer> logitBias,
        Boolean stream
) {

    public ChatCompletionRequest withStream() {
        return new ChatCompletionRequest(
                model,
                n,
                maxTokens,
                frequencyPenalty,
                presencePenalty,
                temperature,
                topP,
                stop,
                messages,
                logprobs,
                topLogprobs,
                user,
                seed,
                logitBias,
                true);
    }

}
