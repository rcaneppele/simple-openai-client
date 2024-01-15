package br.com.rcaneppele.openai.endpoints.run.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Map;

public record RunStep(
    String id,
    String object,
    @JsonProperty("created_at")
    Instant createAt,
    @JsonProperty("assistant_id")
    String assistantId,
    @JsonProperty("thread_id")
    String threadId,
    @JsonProperty("run_id")
    String runId,
    RunStepType type,
    RunStepStatus status,
    @JsonProperty("step_details")
    RunStepDetails stepDetails,
    @JsonProperty("last_error")
    LastError lastError,
    @JsonProperty("expired_at")
    Instant expiredAt,
    @JsonProperty("cancelled_at")
    Instant cancelledAt,
    @JsonProperty("failed_at")
    Instant failedAt,
    @JsonProperty("completed_at")
    Instant completedAt,
    Map<String, String> metadata
) {}
