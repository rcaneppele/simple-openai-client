package br.com.rcaneppele.openai.endpoints.assistant.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.endpoints.assistant.request.CreateAssistantFileRequest;
import br.com.rcaneppele.openai.endpoints.assistant.response.AssistantFile;

import java.time.Duration;

public class CreateAssistantFileRequestSender extends AssistantRequestSender<CreateAssistantFileRequest, AssistantFile> {

    private final String assistantId;

    public CreateAssistantFileRequestSender(String apiBaseUrl, Duration timeout, String apiKey, String assistantId) {
        super(apiBaseUrl, timeout, apiKey);
        this.assistantId = assistantId;
    }

    @Override
    protected String endpointUri() {
        return super.endpointUri() + "/" + this.assistantId + "/files";
    }

    @Override
    protected HttpMethod httpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected Class<CreateAssistantFileRequest> requestType() {
        return CreateAssistantFileRequest.class;
    }

    @Override
    protected Class<AssistantFile> responseType() {
        return AssistantFile.class;
    }

}
