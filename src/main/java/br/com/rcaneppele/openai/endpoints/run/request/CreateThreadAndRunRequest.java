package br.com.rcaneppele.openai.endpoints.run.request;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.endpoints.assistant.tools.Tool;
import br.com.rcaneppele.openai.endpoints.thread.request.CreateThreadRequest;

import java.util.Map;
import java.util.Set;

public record CreateThreadAndRunRequest(
        String assistantId,
        CreateThreadRequest thread,
        OpenAIModel model,
        String instructions,
        Set<Tool> tools,
        Map<String, String> metadata
) {}
