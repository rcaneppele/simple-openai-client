package br.com.rcaneppele.openai.endpoints.assistant.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.QueryParameters;
import br.com.rcaneppele.openai.endpoints.assistant.response.ListOfAssistants;

import java.time.Duration;

public class ListAssistantsRequestSender extends AssistantRequestSender<QueryParameters, ListOfAssistants> {

    private QueryParameters queryParameters;

    public ListAssistantsRequestSender(String apiBaseUrl, Duration timeout, String apiKey) {
        super(apiBaseUrl, timeout, apiKey);
    }

    @Override
    public ListOfAssistants sendRequest(QueryParameters queryParameters) {
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
    protected Class<ListOfAssistants> responseType() {
        return ListOfAssistants.class;
    }

}
