package br.com.rcaneppele.openai.endpoints.run.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.endpoints.run.request.ModifyRunRequest;
import br.com.rcaneppele.openai.endpoints.run.response.Run;

import java.time.Duration;

public class ModifyRunRequestSender extends RunRequestSender<ModifyRunRequest, Run> {

    private final String runId;

    public ModifyRunRequestSender(String apiBaseUrl, Duration timeout, String apiKey, String threadId, String runId) {
        super(apiBaseUrl, timeout, apiKey, threadId);
        this.runId = runId;
    }

    @Override
    protected String endpointUri() {
        return super.endpointUri() +"/" +this.runId;
    }

    @Override
    protected HttpMethod httpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected Class<ModifyRunRequest> requestType() {
        return ModifyRunRequest.class;
    }

    @Override
    protected Class<Run> responseType() {
        return Run.class;
    }

}
