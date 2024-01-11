package br.com.rcaneppele.openai.endpoints.assistant.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.QueryParameters;
import br.com.rcaneppele.openai.endpoints.assistant.response.ListOfAssistantFiles;

import java.time.Duration;

public class ListAssistantFilesRequestSender extends AssistantRequestSender<QueryParameters, ListOfAssistantFiles> {

    private final String assistantId;
    private QueryParameters queryParameters;

    public ListAssistantFilesRequestSender(String apiBaseUrl, Duration timeout, String apiKey, String assistantId) {
        super(apiBaseUrl, timeout, apiKey);
        this.assistantId = assistantId;
    }

    @Override
    protected String endpointUri() {
        return super.endpointUri() + "/" + this.assistantId + "/files";
    }

    @Override
    public ListOfAssistantFiles sendRequest(QueryParameters queryParameters) {
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
    protected Class<ListOfAssistantFiles> responseType() {
        return ListOfAssistantFiles.class;
    }

}
