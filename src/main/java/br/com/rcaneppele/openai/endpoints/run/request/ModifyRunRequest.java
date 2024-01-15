package br.com.rcaneppele.openai.endpoints.run.request;

import java.util.Map;

public record ModifyRunRequest(
        String threadId,
        String runId,
        Map<String, String> metadata
) {}
