package br.com.rcaneppele.openai.endpoints.thread.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record ModifyThreadRequest(
        @JsonProperty("thread_id")
        String threadId,
        Map<String, String> metadata
) implements ThreadRequest {}
