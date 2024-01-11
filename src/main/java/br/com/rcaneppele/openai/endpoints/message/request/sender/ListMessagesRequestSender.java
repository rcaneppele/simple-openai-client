package br.com.rcaneppele.openai.endpoints.message.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.QueryParameters;
import br.com.rcaneppele.openai.endpoints.message.response.ListOfMessages;

import java.time.Duration;

public class ListMessagesRequestSender extends MessageRequestSender<QueryParameters, ListOfMessages> {

    private QueryParameters queryParameters;

    public ListMessagesRequestSender(String apiBaseUrl, Duration timeout, String apiKey, String threadId) {
        super(apiBaseUrl, timeout, apiKey, threadId);
    }

    @Override
    public ListOfMessages sendRequest(QueryParameters queryParameters) {
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
    protected Class<ListOfMessages> responseType() {
        return ListOfMessages.class;
    }

}
