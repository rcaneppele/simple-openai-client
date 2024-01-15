package br.com.rcaneppele.openai.endpoints.run.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record ToolCall(
    String id,
    String type,
    @JsonProperty("code_interpreter")
    ToolCallCodeInterpreter codeInterpreter,
    Map<String, String> retrieval,
    ToolCallFunction function
) {}
