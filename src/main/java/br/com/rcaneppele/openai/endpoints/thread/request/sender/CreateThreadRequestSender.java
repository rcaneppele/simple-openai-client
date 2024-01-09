package br.com.rcaneppele.openai.endpoints.thread.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.endpoints.thread.request.CreateThreadRequest;
import br.com.rcaneppele.openai.endpoints.thread.response.Thread;

import java.time.Duration;

public class CreateThreadRequestSender extends ThreadRequestSender<CreateThreadRequest, Thread> {

    public CreateThreadRequestSender(String apiBaseUrl, Duration timeout, String apiKey) {
        super(apiBaseUrl, timeout, apiKey);
    }

    @Override
    protected HttpMethod httpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected Class<CreateThreadRequest> requestType() {
        return CreateThreadRequest.class;
    }

    @Override
    protected Class<Thread> responseType() {
        return Thread.class;
    }

}
