package br.com.rcaneppele.openai.endpoints.run.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.endpoints.run.response.RunStep;

import java.time.Duration;

public class RetrieveRunStepRequestSender extends RunRequestSender<Void, RunStep> {

    private final String runId;
    private final String stepId;

    public RetrieveRunStepRequestSender(String apiBaseUrl, Duration timeout, String apiKey, String threadId, String runId, String stepId) {
        super(apiBaseUrl, timeout, apiKey, threadId);
        this.runId = runId;
        this.stepId = stepId;
    }

    @Override
    protected String endpointUri() {
        return super.endpointUri() + "/" +this.runId +"/steps/" +this.stepId;
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
    protected Class<RunStep> responseType() {
        return RunStep.class;
    }

}
