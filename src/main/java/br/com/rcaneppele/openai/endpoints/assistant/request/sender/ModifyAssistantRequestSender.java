package br.com.rcaneppele.openai.endpoints.assistant.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.endpoints.assistant.request.ModifyAssistantRequest;
import br.com.rcaneppele.openai.endpoints.assistant.response.Assistant;

import java.time.Duration;

public class ModifyAssistantRequestSender extends AssistantRequestSender<ModifyAssistantRequest, Assistant> {

    private String assistantId;

    public ModifyAssistantRequestSender(String apiBaseUrl, Duration timeout, String apiKey, String assistantId) {
        super(apiBaseUrl, timeout, apiKey);
        this.assistantId = assistantId;
    }

    @Override
    protected String endpointUri() {
        return super.endpointUri() +"/" +this.assistantId;
    }

    @Override
    protected HttpMethod httpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected Class<ModifyAssistantRequest> requestType() {
        return ModifyAssistantRequest.class;
    }

    @Override
    protected Class<Assistant> responseType() {
        return Assistant.class;
    }

}
