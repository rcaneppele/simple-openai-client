package br.com.rcaneppele.openai.endpoints.run.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record RunStepDetails(
    String type,
    @JsonProperty("message_creation")
    MessageCreation messageCreation,
    @JsonProperty("tool_calls")
    List<ToolCall> toolCalls
) {}
