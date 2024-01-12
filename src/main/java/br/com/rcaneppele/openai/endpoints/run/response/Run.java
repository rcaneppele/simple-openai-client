package br.com.rcaneppele.openai.endpoints.run.response;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.endpoints.assistant.tools.Tool;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

public record Run(
    String id,
    String object,
    @JsonProperty("created_at")
    Instant createAt,
    @JsonProperty("assistant_id")
    String assistantId,
    @JsonProperty("thread_id")
    String threadId,
    RunStatus status,
    @JsonProperty("started_at")
    Instant startedAt,
    @JsonProperty("expires_at")
    Instant expiresAt,
    @JsonProperty("cancelled_at")
    Instant cancelledAt,
    @JsonProperty("failed_at")
    Instant failedAt,
    @JsonProperty("completed_at")
    Instant completedAt,
    @JsonProperty("last_error")
    String lastError,
    OpenAIModel model,
    String instructions,
    Set<Tool> tools,
    @JsonProperty("file_ids")
    Set<String> fileIds,
    Map<String, String> metadata
) {}
