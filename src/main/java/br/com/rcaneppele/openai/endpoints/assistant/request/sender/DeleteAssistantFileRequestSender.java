package br.com.rcaneppele.openai.endpoints.assistant.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.endpoints.assistant.response.DeletionStatus;

import java.time.Duration;

public class DeleteAssistantFileRequestSender extends AssistantRequestSender<Void, DeletionStatus> {

    private final String assistantId;
    private final String fileId;

    public DeleteAssistantFileRequestSender(String apiBaseUrl, Duration timeout, String apiKey, String assistantId, String fileId) {
        super(apiBaseUrl, timeout, apiKey);
        this.assistantId = assistantId;
        this.fileId = fileId;
    }

    @Override
    protected String endpointUri() {
        return super.endpointUri() + "/" + this.assistantId +"/files/" +this.fileId;
    }

    @Override
    protected HttpMethod httpMethod() {
        return HttpMethod.DELETE;
    }

    @Override
    protected Class<Void> requestType() {
        return Void.class;
    }

    @Override
    protected Class<DeletionStatus> responseType() {
        return DeletionStatus.class;
    }

}
