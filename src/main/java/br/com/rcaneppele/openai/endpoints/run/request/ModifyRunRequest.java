package br.com.rcaneppele.openai.endpoints.run.request;

import java.util.Map;

public record ModifyRunRequest(
        Map<String, String> metadata
) {}
