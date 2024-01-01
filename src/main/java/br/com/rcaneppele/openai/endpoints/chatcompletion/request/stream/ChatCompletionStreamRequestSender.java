package br.com.rcaneppele.openai.endpoints.chatcompletion.request.stream;

import br.com.rcaneppele.openai.common.response.stream.StreamResponseBuilder;
import br.com.rcaneppele.openai.endpoints.chatcompletion.request.ChatCompletionRequest;
import br.com.rcaneppele.openai.endpoints.chatcompletion.request.ChatCompletionRequestSender;
import br.com.rcaneppele.openai.endpoints.chatcompletion.response.ChatCompletionResponse;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.time.Duration;

public class ChatCompletionStreamRequestSender extends ChatCompletionRequestSender {

    private final StreamResponseBuilder builder;

    public ChatCompletionStreamRequestSender(String apiBaseUrl, Duration timeout, String apiKey) {
        super(apiBaseUrl, timeout, apiKey);
        this.builder = new StreamResponseBuilder();
    }

    public Observable<ChatCompletionResponse> sendStreamRequest(ChatCompletionRequest request) {
        return Observable.create(emitter -> {
            var json = jsonConverter.convertRequestToJson(request);
            var httpRequest = new Request.Builder()
                    .url(this.apiBaseUrl + endpointUri())
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Accept", "text/event-stream")
                    .post(RequestBody.create(json, MediaType.parse("application/json")))
                    .build();

            try (var response = httpClient.newCall(httpRequest).execute()) {
                builder.build(response, emitter);
            } catch (Exception e) {
                if (!emitter.isDisposed()) {
                    emitter.onError(e);
                }
            }
        });
    }

}
