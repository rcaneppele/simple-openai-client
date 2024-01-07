package br.com.rcaneppele.openai.endpoints.threads.request;

import br.com.rcaneppele.openai.endpoints.threads.messages.request.CreateMessageRequest;

import java.util.List;
import java.util.Map;

public record CreateThreadRequest(
        List<CreateMessageRequest> messages,
        Map<String, String> metadata
) {}
