package br.com.rcaneppele.openai.endpoints.assistant.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.endpoints.assistant.request.ListAssistantsRequest;
import br.com.rcaneppele.openai.endpoints.assistant.response.ListAssistantsResponse;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

public class ListAssistantsRequestSender extends AssistantRequestSender<ListAssistantsRequest, ListAssistantsResponse> {

    private final Map<String, Object> queryParam = new LinkedHashMap<>();

    public ListAssistantsRequestSender(String apiBaseUrl, Duration timeout, String apiKey) {
        super(apiBaseUrl, timeout, apiKey);
    }

    @Override
    public ListAssistantsResponse sendRequest(ListAssistantsRequest request) {
        addQueryParamIfNotNull("limit", request.limit());
        addQueryParamIfNotNull("order", request.order());
        addQueryParamIfNotNull("after", request.after());
        addQueryParamIfNotNull("before", request.before());

        return super.sendRequest(request);
    }

    @Override
    protected HttpMethod httpMethod() {
        return HttpMethod.GET;
    }

    @Override
    protected Class<ListAssistantsRequest> requestType() {
        return ListAssistantsRequest.class;
    }

    @Override
    protected Class<ListAssistantsResponse> responseType() {
        return ListAssistantsResponse.class;
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
