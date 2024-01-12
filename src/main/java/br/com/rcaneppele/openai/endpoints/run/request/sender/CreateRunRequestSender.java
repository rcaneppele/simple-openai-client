package br.com.rcaneppele.openai.endpoints.run.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.endpoints.run.request.CreateRunRequest;
import br.com.rcaneppele.openai.endpoints.run.response.Run;

import java.time.Duration;

public class CreateRunRequestSender extends RunRequestSender<CreateRunRequest, Run> {

    public CreateRunRequestSender(String apiBaseUrl, Duration timeout, String apiKey, String threadId) {
        super(apiBaseUrl, timeout, apiKey, threadId);
    }

    @Override
    protected HttpMethod httpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected Class<CreateRunRequest> requestType() {
        return CreateRunRequest.class;
    }

    @Override
    protected Class<Run> responseType() {
        return Run.class;
    }

}
