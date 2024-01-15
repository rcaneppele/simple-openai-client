package br.com.rcaneppele.openai.endpoints.run.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.QueryParameters;
import br.com.rcaneppele.openai.endpoints.run.response.ListOfRunSteps;

import java.time.Duration;

public class ListRunStepsRequestSender extends RunRequestSender<QueryParameters, ListOfRunSteps> {

    private final String runId;
    private QueryParameters queryParameters;

    public ListRunStepsRequestSender(String apiBaseUrl, Duration timeout, String apiKey, String threadId, String runId) {
        super(apiBaseUrl, timeout, apiKey, threadId);
        this.runId = runId;
    }

    @Override
    protected String endpointUri() {
        return super.endpointUri() +"/" +this.runId +"/steps";
    }

    @Override
    public ListOfRunSteps sendRequest(QueryParameters queryParameters) {
        this.queryParameters = queryParameters;
        return super.sendRequest(null);
    }

    @Override
    protected QueryParameters queryParameters() {
        return this.queryParameters;
    }

    @Override
    protected HttpMethod httpMethod() {
        return HttpMethod.GET;
    }

    @Override
    protected Class<QueryParameters> requestType() {
        return QueryParameters.class;
    }

    @Override
    protected Class<ListOfRunSteps> responseType() {
        return ListOfRunSteps.class;
    }

}
