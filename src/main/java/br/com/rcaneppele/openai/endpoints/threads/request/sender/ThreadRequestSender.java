package br.com.rcaneppele.openai.endpoints.threads.request.sender;

import br.com.rcaneppele.openai.common.request.RequestSender;

import java.time.Duration;
import java.util.Map;

abstract class ThreadRequestSender<I, O> extends RequestSender<I, O> {

    public ThreadRequestSender(String apiBaseUrl, Duration timeout, String apiKey) {
        super(apiBaseUrl, timeout, apiKey);
    }

    @Override
    protected Map<String, String> additionalHeaders() {
        return Map.of("OpenAI-Beta", "assistants=v1");
    }

    @Override
    protected String endpointUri() {
        return "threads";
    }

}
