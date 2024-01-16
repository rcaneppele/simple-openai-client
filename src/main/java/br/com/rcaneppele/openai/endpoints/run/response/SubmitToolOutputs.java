package br.com.rcaneppele.openai.endpoints.run.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SubmitToolOutputs(
    @JsonProperty("tool_calls")
    List<ToolCall> toolCalls
) {}
