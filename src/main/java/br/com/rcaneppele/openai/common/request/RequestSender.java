package br.com.rcaneppele.openai.common.request;

import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.common.response.ResponseBuilder;
import br.com.rcaneppele.openai.http.HttpClientBuilder;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

public abstract class RequestSender<I, O> {

    protected final OkHttpClient httpClient;
    protected final String apiBaseUrl;
    protected final String apiKey;
    protected final JsonConverter<I> jsonConverter;
    protected final ResponseBuilder<O> responseBuilder;

    public RequestSender(String apiBaseUrl, Duration timeout, String apiKey) {
        this.httpClient = new HttpClientBuilder().build(timeout);
        this.apiBaseUrl = apiBaseUrl;
        this.apiKey = apiKey;
        this.jsonConverter = new JsonConverter<I>(requestType());
        this.responseBuilder = new ResponseBuilder<O>(responseType());
    }

    protected Map<String, String> additionalHeaders() {
        return Map.of();
    }
    protected QueryParameters queryParameters() {
        return null;
    }
    protected abstract String endpointUri();
    protected abstract Class<I> requestType();
    protected abstract Class<O> responseType();
    protected abstract HttpMethod httpMethod();

    public O sendRequest(I request) {
        var url = this.apiBaseUrl + endpointUri();
        var builder = new Request.Builder().header("Authorization", "Bearer " + this.apiKey);

        switch (httpMethod()) {
            case GET -> {
                url = appendQueryParameters(url);
                builder.get();
            }

            case POST -> {
                if (request != null) {
                    var json = jsonConverter.convertRequestToJson(request);
                    builder.post(RequestBody.create(json, MediaType.parse("application/json")));
                } else {
                    builder.post(RequestBody.create("", null));
                }
            }

            case DELETE -> {
                builder.delete();
            }
        }

        additionalHeaders().forEach(builder::header);
        var httpRequest = builder.url(url).build();

        try {
            var response = httpClient.newCall(httpRequest).execute();
            return responseBuilder.build(response);
        } catch (IOException e) {
            throw new RuntimeException("Error sending request", e);
        }
    }

    private String appendQueryParameters(String url) {
        var parameters = queryParameters();
        if (parameters == null) {
            return url;
        }

        var builder = new StringBuilder(url);
        builder.append("?");

        addQueryParamIfNotNull(builder,"limit", parameters.limit());
        addQueryParamIfNotNull(builder,"order", parameters.order());
        addQueryParamIfNotNull(builder,"after", parameters.after());
        addQueryParamIfNotNull(builder,"before", parameters.before());

        // Remove the trailing "&"
        builder.setLength(builder.length() - 1);

        return builder.toString();
    }

    private void addQueryParamIfNotNull(StringBuilder builder, String paramName, Object paramValue) {
        if (paramValue != null) {
            builder.append(paramName).append("=").append(paramValue).append("&");
        }
    }

}
