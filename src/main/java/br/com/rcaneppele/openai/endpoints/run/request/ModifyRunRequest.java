package br.com.rcaneppele.openai.endpoints.run.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record ModifyRunRequest(
        @JsonProperty("thread_id")
        String threadId,
        @JsonProperty("run_id")
        String runId,
        Map<String, String> metadata
) {}
