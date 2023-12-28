package br.com.rcaneppele.openai.error;

import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.error.exception.APIKeyException;
import br.com.rcaneppele.openai.error.exception.BadRequestException;
import br.com.rcaneppele.openai.error.exception.RateLimitException;
import br.com.rcaneppele.openai.error.exception.ServiceUnavailableException;
import okhttp3.Response;

public class APIErrorHandler {

    private final JsonConverter jsonConverter;

    public APIErrorHandler() {
        this.jsonConverter = new JsonConverter();
    }

    public void handleError(Response response) {
        var error = getApiErrorOfResponse(response);
        switch (response.code()) {
            case 400 -> throw new BadRequestException("Bad Request error: " + error.message());
            case 401 -> throw new APIKeyException("API Key error: " + error.message());
            case 429 -> throw new RateLimitException("Rate Limit error: " + error.message());
            case 503 -> throw new ServiceUnavailableException("Service Unavailable error: " + error.message());
            default -> throw new RuntimeException("Unknown error: " + error.message());
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
