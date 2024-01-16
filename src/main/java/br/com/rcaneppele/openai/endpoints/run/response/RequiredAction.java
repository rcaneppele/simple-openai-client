package br.com.rcaneppele.openai.endpoints.run.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RequiredAction(
    String type,
    @JsonProperty("submit_tool_outputs")
    SubmitToolOutputs submitToolOutputs
) {}
