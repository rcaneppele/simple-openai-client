package br.com.rcaneppele.openai.endpoints.thread.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.endpoints.thread.request.ModifyThreadRequest;
import br.com.rcaneppele.openai.endpoints.thread.response.Thread;

import java.time.Duration;

public class ModifyThreadRequestSender extends ThreadRequestSender<ModifyThreadRequest, Thread> {

    private final String threadId;

    public ModifyThreadRequestSender(String apiBaseUrl, Duration timeout, String apiKey, String threadId) {
        super(apiBaseUrl, timeout, apiKey);
        this.threadId = threadId;
    }

    @Override
    protected String endpointUri() {
        return super.endpointUri() + "/" + this.threadId;
    }

    @Override
    protected HttpMethod httpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected Class<ModifyThreadRequest> requestType() {
        return ModifyThreadRequest.class;
    }

    @Override
    protected Class<Thread> responseType() {
        return Thread.class;
    }

}
