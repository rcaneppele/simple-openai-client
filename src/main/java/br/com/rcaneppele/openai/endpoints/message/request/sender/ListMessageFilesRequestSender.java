package br.com.rcaneppele.openai.endpoints.message.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.QueryParameters;
import br.com.rcaneppele.openai.endpoints.message.response.ListOfMessageFiles;

import java.time.Duration;

public class ListMessageFilesRequestSender extends MessageRequestSender<QueryParameters, ListOfMessageFiles> {

    private final String messageId;
    private QueryParameters queryParameters;

    public ListMessageFilesRequestSender(String apiBaseUrl, Duration timeout, String apiKey, String threadId, String messageId) {
        super(apiBaseUrl, timeout, apiKey, threadId);
        this.messageId = messageId;
    }

    @Override
    protected String endpointUri() {
        return super.endpointUri() + "/" +this.messageId + "/files";
    }

    @Override
    public ListOfMessageFiles sendRequest(QueryParameters queryParameters) {
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
    protected Class<ListOfMessageFiles> responseType() {
        return ListOfMessageFiles.class;
    }

}
