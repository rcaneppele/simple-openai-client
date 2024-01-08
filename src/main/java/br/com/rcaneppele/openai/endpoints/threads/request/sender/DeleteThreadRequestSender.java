package br.com.rcaneppele.openai.endpoints.threads.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.endpoints.assistant.response.DeletionStatus;

import java.time.Duration;

public class DeleteThreadRequestSender extends ThreadRequestSender<Void, DeletionStatus> {

    private final String threadId;

    public DeleteThreadRequestSender(String apiBaseUrl, Duration timeout, String apiKey, String threadId) {
        super(apiBaseUrl, timeout, apiKey);
        this.threadId = threadId;
    }

    @Override
    protected String endpointUri() {
        return super.endpointUri() + "/" + this.threadId;
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
