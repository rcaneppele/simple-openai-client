package br.com.rcaneppele.openai.endpoints.run.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.endpoints.run.request.SubmitToolOutputsToRunRequest;
import br.com.rcaneppele.openai.endpoints.run.response.Run;

import java.time.Duration;

public class SubmitToolOutputsToRunRequestSender extends RunRequestSender<SubmitToolOutputsToRunRequest, Run> {

    private final String runId;

    public SubmitToolOutputsToRunRequestSender(String apiBaseUrl, Duration timeout, String apiKey, String threadId, String runId) {
        super(apiBaseUrl, timeout, apiKey, threadId);
        this.runId = runId;
    }

    @Override
    protected String endpointUri() {
        return super.endpointUri() +"/" +this.runId +"/submit_tool_outputs";
    }

    @Override
    protected HttpMethod httpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected Class<SubmitToolOutputsToRunRequest> requestType() {
        return SubmitToolOutputsToRunRequest.class;
    }

    @Override
    protected Class<Run> responseType() {
        return Run.class;
    }

}
