package br.com.rcaneppele.openai.endpoints.assistant.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.endpoints.assistant.response.Assistant;

import java.time.Duration;

public class RetrieveAssistantRequestSender extends AssistantRequestSender<Void, Assistant> {

    private final String assistantId;

    public RetrieveAssistantRequestSender(String apiBaseUrl, Duration timeout, String apiKey, String assistantId) {
        super(apiBaseUrl, timeout, apiKey);
        this.assistantId = assistantId;
    }

    @Override
    protected String endpointUri() {
        return super.endpointUri() + "/" + this.assistantId;
    }

    @Override
    protected HttpMethod httpMethod() {
        return HttpMethod.GET;
    }

    @Override
    protected Class<Void> requestType() {
        return Void.class;
    }

    @Override
    protected Class<Assistant> responseType() {
        return Assistant.class;
    }

}
