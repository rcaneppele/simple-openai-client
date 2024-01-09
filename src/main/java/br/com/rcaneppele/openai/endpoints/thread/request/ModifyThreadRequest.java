package br.com.rcaneppele.openai.endpoints.thread.request;

import java.util.Map;

public record ModifyThreadRequest(
        String threadId,
        Map<String, String> metadata
) implements ThreadRequest {}
