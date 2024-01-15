package br.com.rcaneppele.openai.endpoints.run.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.QueryParameters;
import br.com.rcaneppele.openai.endpoints.run.response.ListOfRuns;

import java.time.Duration;

public class ListRunsRequestSender extends RunRequestSender<QueryParameters, ListOfRuns> {

    private QueryParameters queryParameters;

    public ListRunsRequestSender(String apiBaseUrl, Duration timeout, String apiKey, String threadId) {
        super(apiBaseUrl, timeout, apiKey, threadId);
    }

    @Override
    public ListOfRuns sendRequest(QueryParameters queryParameters) {
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
    protected Class<ListOfRuns> responseType() {
        return ListOfRuns.class;
    }

}
