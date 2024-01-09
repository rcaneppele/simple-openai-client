package br.com.rcaneppele.openai.endpoints.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record Message(
        String id,
        String object,
        @JsonProperty("created_at")
        Instant createdAt,
        @JsonProperty("thread_id")
        String threadId,
        String role,
        List<MessageContent> content,
        @JsonProperty("assistant_id")
        String assistantId,
        @JsonProperty("run_id")
        String runId,
        @JsonProperty("file_ids")
        Set<String> fileIds,
        Map<String, String> metadata
) {

    public String firstMessageContentText() {
        return getMessageContentText(0);
    }

    public String lastMessageContentText() {
        if (this.content == null || this.content.isEmpty()) {
            return null;
        }

        return getMessageContentText(this.content.size() - 1);
    }

    private String getMessageContentText(int choice) {
        if (this.content == null || this.content.isEmpty()) {
            return null;
        }

        var content = this.content.get(choice);
        return content.text() != null ? content.text().value() : null;
    }

    public String firstMessageContentImage() {
        return getMessageContentImage(0);
    }

    public String lastMessageContentImage() {
        if (this.content == null || this.content.isEmpty()) {
            return null;
        }

        return getMessageContentImage(this.content.size() - 1);
    }

    private String getMessageContentImage(int choice) {
        if (this.content == null || this.content.isEmpty()) {
            return null;
        }

        var content = this.content.get(choice);
        return content.imageFile() != null ? content.imageFile().fileId() : null;
    }

}
