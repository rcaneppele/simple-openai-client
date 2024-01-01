package br.com.rcaneppele.openai.endpoints.assistant.request.sender;

import br.com.rcaneppele.openai.common.request.RequestSender;

import java.time.Duration;
import java.util.Map;

abstract class AssistantRequestSender<I, O> extends RequestSender<I, O> {

    public AssistantRequestSender(String apiBaseUrl, Duration timeout, String apiKey) {
        super(apiBaseUrl, timeout, apiKey);
    }

    @Override
    protected Map<String, String> optionalHeaders() {
        return Map.of("OpenAI-Beta", "assistants=v1");
    }

    @Override
    protected String endpointUri() {
        return "assistants";
    }

}
