package br.com.rcaneppele.openai.endpoints.assistant.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.QueryParameters;
import br.com.rcaneppele.openai.endpoints.assistant.response.ListOfAssistants;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

public class ListAssistantsRequestSender extends AssistantRequestSender<QueryParameters, ListOfAssistants> {

    private final Map<String, Object> queryParam = new LinkedHashMap<>();

    public ListAssistantsRequestSender(String apiBaseUrl, Duration timeout, String apiKey) {
        super(apiBaseUrl, timeout, apiKey);
    }

    @Override
    public ListOfAssistants sendRequest(QueryParameters parameters) {
        if (parameters != null) {
            addQueryParamIfNotNull("limit", parameters.limit());
            addQueryParamIfNotNull("order", parameters.order());
            addQueryParamIfNotNull("after", parameters.after());
            addQueryParamIfNotNull("before", parameters.before());
        }

        return super.sendRequest(parameters);
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

    @Override
    protected Map<String, Object> queryParams() {
        return this.queryParam;
    }

    private void addQueryParamIfNotNull(String paramName, Object paramValue) {
        if (paramValue != null) {
            queryParam.put(paramName, paramValue);
        }
    }

}
