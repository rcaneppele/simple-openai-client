package br.com.rcaneppele.openai.endpoints.run.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ToolOutput(
    @JsonProperty("tool_call_id")
    String toolCallId,
    String output
) {}
