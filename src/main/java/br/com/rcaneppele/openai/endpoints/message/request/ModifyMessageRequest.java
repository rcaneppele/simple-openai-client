package br.com.rcaneppele.openai.endpoints.message.request;

import java.util.Map;

public record ModifyMessageRequest(
        Map<String, String> metadata
) {}
