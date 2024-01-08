package br.com.rcaneppele.openai.endpoints.threads.request;

import java.util.Map;

public record ModifyThreadRequest(
        String threadId,
        Map<String, String> metadata
) implements ThreadRequest {}
