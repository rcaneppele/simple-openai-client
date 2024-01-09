package br.com.rcaneppele.openai.endpoints.message.request.sender;

import br.com.rcaneppele.openai.common.request.RequestSender;

import java.time.Duration;
import java.util.Map;

abstract class MessageRequestSender<I, O> extends RequestSender<I, O> {

    private String threadId;

    public MessageRequestSender(String apiBaseUrl, Duration timeout, String apiKey, String threadId) {
        super(apiBaseUrl, timeout, apiKey);
        this.threadId = threadId;
    }

    @Override
    protected Map<String, String> additionalHeaders() {
        return Map.of("OpenAI-Beta", "assistants=v1");
    }

    @Override
    protected String endpointUri() {
        return "threads/" + this.threadId +"/messages";
    }

}
