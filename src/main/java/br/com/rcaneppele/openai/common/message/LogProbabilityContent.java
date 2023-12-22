package br.com.rcaneppele.openai.common.message;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record LogProbabilityContent(
        String token,
        Double logprob,
        List<Integer> bytes,
        @JsonProperty("top_logprobs")
        List<LogProbabilityContent> topLogProbability
        ) {
}
