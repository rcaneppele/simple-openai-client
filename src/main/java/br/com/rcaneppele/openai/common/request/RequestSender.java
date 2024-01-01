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

    public O sendRequest(I request) {
        if (request == null) {
            throw new IllegalArgumentException("Request object is required!");
        }

        var json = jsonConverter.convertRequestToJson(request);
        var builder = new Request.Builder()
                .url(this.apiBaseUrl + endpointUri())
                .header("Authorization", "Bearer " + this.apiKey)
                .post(RequestBody.create(json, MediaType.parse("application/json")));

        optionalHeaders().forEach(builder::header);
        var httpRequest = builder.build();

        try {
            var response = httpClient.newCall(httpRequest).execute();
            return responseBuilder.build(response);
        } catch (IOException e) {
            throw new RuntimeException("Error sending request", e);
        }
    }

    protected Map<String, String> optionalHeaders() {
        return Map.of();
    }

    protected abstract String endpointUri();
    protected abstract Class<I> requestType();
    protected abstract Class<O> responseType();

}
