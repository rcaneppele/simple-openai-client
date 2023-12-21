package br.com.rcaneppele.openai.chatcompletion.request;

import br.com.rcaneppele.openai.chatcompletion.response.ChatCompletionResponse;
import br.com.rcaneppele.openai.chatcompletion.response.ChatCompletionResponseBuilder;
import br.com.rcaneppele.openai.common.JsonConverter;
import br.com.rcaneppele.openai.http.HttpClientBuilder;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.time.Duration;

public final class ChatCompletionRequestSender {

    private static final String CHAT_COMPLETION_URI = "chat/completions";
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

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
                .header("Content-Type", MEDIA_TYPE.type())
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

}
