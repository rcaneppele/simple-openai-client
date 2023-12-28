package br.com.rcaneppele.openai.chatcompletion.response;

import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.error.APIErrorHandler;
import io.reactivex.rxjava3.core.ObservableEmitter;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.IOException;

public class ChatCompletionResponseBuilder {

    private final JsonConverter jsonConverter;
    private final APIErrorHandler errorHandler;

    public ChatCompletionResponseBuilder() {
        this.jsonConverter = new JsonConverter();
        this.errorHandler = new APIErrorHandler();
    }

    public ChatCompletionResponse build(Response response) {
        if (!response.isSuccessful()) {
            errorHandler.handleError(response);
            return null;
        }

        try {
            var json = response.body().string();
            return jsonConverter.convertJsonToChatCompletionResponse(json);
        } catch (IOException e) {
            throw new RuntimeException("Error reading json from Chat Completion Response", e);
        }

    }

    public void buildForStream(Response response, ObservableEmitter<ChatCompletionResponse> emitter) throws IOException {
        if (!response.isSuccessful()) {
            errorHandler.handleError(response);
        }

        var reader = new BufferedReader(response.body().charStream());
        String line;
        while ((line = reader.readLine()) != null && !emitter.isDisposed()) {
            if (line.equals("data: [DONE]")) {
                emitter.onComplete();
                break;
            }

            if (line.startsWith("data: ")) {
                var json = line.substring(6).trim();
                if (!json.isEmpty()) {
                    var partialResponse = jsonConverter.convertJsonToChatCompletionResponse(json);
                    emitter.onNext(partialResponse);
                }
            }
        }
    }

}
