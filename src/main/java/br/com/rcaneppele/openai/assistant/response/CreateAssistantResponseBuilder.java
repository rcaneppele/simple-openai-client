package br.com.rcaneppele.openai.assistant.response;

import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.error.APIErrorHandler;
import okhttp3.Response;

import java.io.IOException;

public class CreateAssistantResponseBuilder {

    private final JsonConverter<CreateAssistantResponse> jsonConverter;
    private final APIErrorHandler errorHandler;

    public CreateAssistantResponseBuilder() {
        this.jsonConverter = new JsonConverter(CreateAssistantResponse.class);
        this.errorHandler = new APIErrorHandler();
    }

    public CreateAssistantResponse build(Response response) {
        if (!response.isSuccessful()) {
            errorHandler.handleError(response);
            return null;
        }

        try {
            var json = response.body().string();
            return jsonConverter.convertJsonToResponse(json);
        } catch (IOException e) {
            throw new RuntimeException("Error reading json from Chat Completion Response", e);
        }

    }

}
