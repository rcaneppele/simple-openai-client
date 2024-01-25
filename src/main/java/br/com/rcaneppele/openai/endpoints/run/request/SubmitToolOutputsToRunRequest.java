package br.com.rcaneppele.openai.endpoints.run.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SubmitToolOutputsToRunRequest(
        @JsonProperty("thread_id")
        String threadId,
        @JsonProperty("run_id")
        String runId,
        @JsonProperty("tool_outputs")
        List<ToolOutput> toolOutputs
) {}
