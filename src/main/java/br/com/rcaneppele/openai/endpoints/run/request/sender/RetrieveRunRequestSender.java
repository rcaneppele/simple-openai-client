package br.com.rcaneppele.openai.endpoints.run.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.endpoints.run.response.Run;

import java.time.Duration;

public class RetrieveRunRequestSender extends RunRequestSender<Void, Run> {

    private final String runId;

    public RetrieveRunRequestSender(String apiBaseUrl, Duration timeout, String apiKey, String threadId, String runId) {
        super(apiBaseUrl, timeout, apiKey, threadId);
        this.runId = runId;
    }

    @Override
    protected String endpointUri() {
        return super.endpointUri() + "/" +this.runId;
    }

    @Override
    protected HttpMethod httpMethod() {
        return HttpMethod.GET;
    }

    @Override
    protected Class<Void> requestType() {
        return Void.class;
    }

    @Override
    protected Class<Run> responseType() {
        return Run.class;
    }

}
