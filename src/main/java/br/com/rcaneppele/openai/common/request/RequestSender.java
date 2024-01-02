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
    protected Map<String, Object> queryParams() {
        return Map.of();
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
                url = appendQueryParams(url);
                builder.get();
            }

            case POST -> {
                if (request == null) {
                    throw new IllegalArgumentException("Request is required!");
                }
                var json = jsonConverter.convertRequestToJson(request);
                builder.post(RequestBody.create(json, MediaType.parse("application/json")));
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

    private String appendQueryParams(String url) {
        var builder = new StringBuilder(url);

        var params = queryParams();
        if (!params.isEmpty()) {
            builder.append("?");

            params.forEach((key, value) -> builder.append(key).append("=").append(value).append("&"));

            // Remove the trailing "&"
            builder.setLength(builder.length() - 1);
        }

        return builder.toString();
    }

}
