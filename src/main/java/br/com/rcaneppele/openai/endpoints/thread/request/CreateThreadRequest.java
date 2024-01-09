package br.com.rcaneppele.openai.endpoints.thread.request;

import br.com.rcaneppele.openai.endpoints.message.request.CreateMessageRequest;

import java.util.List;
import java.util.Map;

public record CreateThreadRequest(
        List<CreateMessageRequest> messages,
        Map<String, String> metadata
) implements ThreadRequest {}
