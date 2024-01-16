package br.com.rcaneppele.openai.endpoints.run.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SubmitToolOutputsToRunRequest(
        String threadId,
        String runId,
        @JsonProperty("tool_outputs")
        List<ToolOutput> toolOutputs
) {}
