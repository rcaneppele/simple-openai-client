package br.com.rcaneppele.openai.error;

import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.error.exception.*;
import okhttp3.Response;

public class APIErrorHandler {

    private final JsonConverter<APIErrorResponse> jsonConverter;

    public APIErrorHandler() {
        this.jsonConverter = new JsonConverter<>(APIErrorResponse.class);
    }

    public void handleError(Response response) {
        var error = getApiErrorOfResponse(response);
        switch (response.code()) {
            case 400 -> throw new BadRequestException("Bad Request error: " + error.message());
            case 404 -> throw new NotFoundException("Not Found error: " + error.message());
            case 401 -> throw new APIKeyException("API Key error: " + error.message());
            case 429 -> throw new RateLimitException("Rate Limit error: " + error.message());
            case 503 -> throw new ServiceUnavailableException("Service Unavailable error: " + error.message());
            default -> throw new RuntimeException("Unknown error: " + error.message());
        }
    }

    private APIError getApiErrorOfResponse(Response response) {
        try {
            var json = response.body().string();
            return jsonConverter.convertJsonToResponse(json).getError();
        } catch (Exception e) {
            throw new RuntimeException("Error during deserialization of response", e);
        }
    }

}
