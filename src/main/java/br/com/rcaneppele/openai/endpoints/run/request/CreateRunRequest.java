package br.com.rcaneppele.openai.endpoints.run.request;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.endpoints.assistant.tools.Tool;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Set;

public record CreateRunRequest(
        @JsonProperty("thread_id")
        String threadId,
        @JsonProperty("assistant_id")
        String assistantId,
        OpenAIModel model,
        String instructions,
        @JsonProperty("additional_instructions")
        String additionalInstructions,
        Set<Tool> tools,
        Map<String, String> metadata
) {}
