package br.com.rcaneppele.openai.common.response.stream;

import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.endpoints.chatcompletion.response.ChatCompletionResponse;
import br.com.rcaneppele.openai.error.APIErrorHandler;
import io.reactivex.rxjava3.core.ObservableEmitter;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.IOException;

public class StreamResponseBuilder {

    private final JsonConverter<ChatCompletionResponse> jsonConverter;
    private final APIErrorHandler errorHandler;

    public StreamResponseBuilder() {
        this.jsonConverter = new JsonConverter<>(ChatCompletionResponse.class);
        this.errorHandler = new APIErrorHandler();
    }

    public void build(Response response, ObservableEmitter<ChatCompletionResponse> emitter) throws IOException {
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
                    var partialResponse = jsonConverter.convertJsonToResponse(json);
                    emitter.onNext(partialResponse);
                }
            }
        }
    }

}
