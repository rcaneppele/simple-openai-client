package br.com.rcaneppele.openai.endpoints.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record ModifyMessageRequest(
        @JsonProperty("thread_id")
        String threadId,
        @JsonProperty("message_id")
        String messageId,
        Map<String, String> metadata
) {}
