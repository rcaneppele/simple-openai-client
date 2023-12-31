package br.com.rcaneppele.openai.common.response;

import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.error.APIErrorHandler;
import okhttp3.Response;

import java.io.IOException;

public class ResponseBuilder<T> {

    private final JsonConverter<T> jsonConverter;
    private final APIErrorHandler errorHandler;

    public ResponseBuilder(Class<T> responseType) {
        this.jsonConverter = new JsonConverter(responseType);
        this.errorHandler = new APIErrorHandler();
    }

    public T build(Response response) {
        if (!response.isSuccessful()) {
            errorHandler.handleError(response);
            return null;
        }

        try {
            var json = response.body().string();
            return jsonConverter.convertJsonToResponse(json);
        } catch (IOException e) {
            throw new RuntimeException("Error converting json to response", e);
        }

    }

}
