package br.com.rcaneppele.openai.endpoints.run.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.endpoints.run.request.CreateThreadAndRunRequest;
import br.com.rcaneppele.openai.endpoints.run.response.Run;

import java.time.Duration;

public class CreateThreadAndRunRequestSender extends RunRequestSender<CreateThreadAndRunRequest, Run> {

    public CreateThreadAndRunRequestSender(String apiBaseUrl, Duration timeout, String apiKey) {
        super(apiBaseUrl, timeout, apiKey, null);
    }

    @Override
    protected String endpointUri() {
        return "threads/runs";
    }

    @Override
    protected HttpMethod httpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected Class<CreateThreadAndRunRequest> requestType() {
        return CreateThreadAndRunRequest.class;
    }

    @Override
    protected Class<Run> responseType() {
        return Run.class;
    }

}
