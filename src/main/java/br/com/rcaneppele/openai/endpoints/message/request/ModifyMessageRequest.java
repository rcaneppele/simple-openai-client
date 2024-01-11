package br.com.rcaneppele.openai.endpoints.message.request;

import java.util.Map;

public record ModifyMessageRequest(
        String threadId,
        String messageId,
        Map<String, String> metadata
) {}
