package br.com.rcaneppele.openai.endpoints.run.response;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.endpoints.assistant.tools.Tool;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;
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
    @JsonProperty("required_action")
    RequiredAction requiredAction,
    @JsonProperty("last_error")
    LastError lastError,
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
    OpenAIModel model,
    String instructions,
    Set<Tool> tools,
    @JsonProperty("file_ids")
    Set<String> fileIds,
    Map<String, String> metadata
) {

    public boolean isInProgress() {
        return this.status == RunStatus.IN_PROGRESS;
    }

    public boolean isCompleted() {
        return this.status == RunStatus.COMPLETED;
    }

    public boolean isExpired() {
        return this.status == RunStatus.EXPIRED;
    }

    public boolean isFailed() {
        return this.status == RunStatus.FAILED;
    }

    public boolean isCancelled() {
        return this.status == RunStatus.CANCELLED;
    }

    public boolean isRequiresAction() {
        return this.status == RunStatus.REQUIRES_ACTION;
    }

    public List<ToolCall> toolsToCall() {
        if (this.requiredAction == null) {
            return null;
        }

        return this.requiredAction.submitToolOutputs().toolCalls();
    }

    public ToolCallFunction firstToolCallFunction() {
        if (this.requiredAction == null) {
            return null;
        }

        return this.requiredAction.submitToolOutputs().toolCalls().get(0).function();
    }

}
