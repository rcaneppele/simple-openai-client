package br.com.rcaneppele.openai.endpoints.assistant.assistant.request;

import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.endpoints.assistant.assistant.response.CreateAssistantResponse;

import java.time.Duration;
import java.util.Map;

public class CreateAssistantRequestSender extends RequestSender<CreateAssistantRequest, CreateAssistantResponse> {

    public CreateAssistantRequestSender(String apiBaseUrl, Duration timeout, String apiKey) {
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

    @Override
    protected Class<CreateAssistantRequest> requestType() {
        return CreateAssistantRequest.class;
    }

    @Override
    protected Class<CreateAssistantResponse> responseType() {
        return CreateAssistantResponse.class;
    }

}
