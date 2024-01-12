package br.com.rcaneppele.openai.endpoints.run.request.sender;

import br.com.rcaneppele.openai.common.request.RequestSender;

import java.time.Duration;
import java.util.Map;

abstract class RunRequestSender<I, O> extends RequestSender<I, O> {

    private String threadId;

    public RunRequestSender(String apiBaseUrl, Duration timeout, String apiKey, String threadId) {
        super(apiBaseUrl, timeout, apiKey);
        this.threadId = threadId;
    }

    @Override
    protected Map<String, String> additionalHeaders() {
        return Map.of("OpenAI-Beta", "assistants=v1");
    }

    @Override
    protected String endpointUri() {
        return "threads/" + this.threadId +"/runs";
    }

}
