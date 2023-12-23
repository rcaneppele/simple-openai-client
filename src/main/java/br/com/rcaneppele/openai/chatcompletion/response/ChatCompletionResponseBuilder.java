package br.com.rcaneppele.openai.chatcompletion.response;

import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.error.APIErrorHandler;
import okhttp3.Response;

import java.io.IOException;

public class ChatCompletionResponseBuilder {

    private final JsonConverter jsonConverter;
    private final APIErrorHandler errorHandler;

    public ChatCompletionResponseBuilder() {
        this.jsonConverter = new JsonConverter();
        this.errorHandler = new APIErrorHandler();
    }

    public ChatCompletionResponse build(Response response) {
        if (response.isSuccessful()) {
            return handleSuccessfullResponse(response);
        }

        errorHandler.handleError(response);
        return null;
    }

    private ChatCompletionResponse handleSuccessfullResponse(Response response) {
        try {
            var json = response.body().string();
            return jsonConverter.convertJsonToChatCompletionResponse(json);
        } catch (IOException e) {
            throw new RuntimeException("Error reading json from Chat Completion Response", e);
        }
    }

}
