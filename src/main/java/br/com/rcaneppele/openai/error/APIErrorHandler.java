package br.com.rcaneppele.openai.error;

import br.com.rcaneppele.openai.common.JsonConverter;
import br.com.rcaneppele.openai.error.exception.APIKeyException;
import br.com.rcaneppele.openai.error.exception.RateLimitException;
import okhttp3.Response;

public class APIErrorHandler {

    private final JsonConverter jsonConverter;

    public APIErrorHandler() {
        this.jsonConverter = new JsonConverter();
    }

    public void handleError(Response response) {
        var error = getApiErrorOfResponse(response);
        switch (response.code()) {
            case 401:
                throw new APIKeyException("API KEY Error: " + error.message());
            case 429:
                throw new RateLimitException("Rate Limit Error: " + error.message());
            case 503:
                throw new APIKeyException("Service Unavailable Error: " + error.message());
            default:
                throw new RuntimeException("Unknown error: " + response.message());
        }
    }

    private APIError getApiErrorOfResponse(Response response) {
        try {
            var json = response.body().string();
            return jsonConverter.convertJsonToApiError(json);
        } catch (Exception e) {
            throw new RuntimeException("Error during deserialization of response", e);
        }
    }

}
