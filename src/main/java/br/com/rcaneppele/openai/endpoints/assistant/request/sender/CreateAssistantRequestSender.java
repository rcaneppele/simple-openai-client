package br.com.rcaneppele.openai.endpoints.assistant.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.endpoints.assistant.request.CreateAssistantRequest;
import br.com.rcaneppele.openai.endpoints.assistant.response.CreateAssistantResponse;

import java.time.Duration;

public class CreateAssistantRequestSender extends AssistantRequestSender<CreateAssistantRequest, CreateAssistantResponse> {

    public CreateAssistantRequestSender(String apiBaseUrl, Duration timeout, String apiKey) {
        super(apiBaseUrl, timeout, apiKey);
    }

    @Override
    protected HttpMethod httpMethod() {
        return HttpMethod.POST;
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
