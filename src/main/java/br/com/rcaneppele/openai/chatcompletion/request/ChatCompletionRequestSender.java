package br.com.rcaneppele.openai.chatcompletion.request;

import br.com.rcaneppele.openai.chatcompletion.response.ChatCompletionResponse;
import br.com.rcaneppele.openai.chatcompletion.response.ChatCompletionResponseBuilder;
import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.http.HttpClientBuilder;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.time.Duration;

public class ChatCompletionRequestSender {

    private static final String CHAT_COMPLETION_URI = "chat/completions";
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json");

    private final OkHttpClient http;
    private final String apiKey;
    private final String chatCompletionUrl;
    private final JsonConverter jsonConverter;

    public ChatCompletionRequestSender(String apiBaseUrl, Duration timeout, String apiKey) {
        this.chatCompletionUrl = apiBaseUrl + CHAT_COMPLETION_URI;
        this.http = new HttpClientBuilder().build(timeout);
        this.apiKey = apiKey;
        this.jsonConverter = new JsonConverter();
    }

    public ChatCompletionResponse sendRequest(ChatCompletionRequest request) {
        var json = jsonConverter.convertChatCompletionRequestToJson(request);
        var httpRequest =  new Request.Builder()
                .url(chatCompletionUrl)
                .header("Authorization", "Bearer " +this.apiKey)
                .post(RequestBody.create(json, MEDIA_TYPE))
                .build();

        try {
            var response = http.newCall(httpRequest).execute();
            return new ChatCompletionResponseBuilder().build(response);
        } catch (IOException e) {
            throw new RuntimeException("Error sending chat completion request", e);
        }
    }

    public Observable<ChatCompletionResponse> sendStreamRequest(final ChatCompletionRequest request) {
        return Observable.create(emitter -> {
            var json = jsonConverter.convertChatCompletionRequestToJson(request);
            var httpRequest = new Request.Builder()
                    .url(chatCompletionUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Accept", "text/event-stream")
                    .post(RequestBody.create(json, MEDIA_TYPE))
                    .build();

            try (var response = http.newCall(httpRequest).execute()) {
                var builder = new ChatCompletionResponseBuilder();
                builder.buildForStream(response, emitter);
            } catch (Exception e) {
                if (!emitter.isDisposed()) {
                    emitter.onError(e);
                }
            }
        });
    }

}
