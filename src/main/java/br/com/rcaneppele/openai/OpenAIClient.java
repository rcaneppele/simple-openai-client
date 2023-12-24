package br.com.rcaneppele.openai;

import br.com.rcaneppele.openai.chatcompletion.request.ChatCompletionRequest;
import br.com.rcaneppele.openai.chatcompletion.response.ChatCompletionResponse;
import br.com.rcaneppele.openai.chatcompletion.response.ChatCompletionResponseBuilder;
import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.http.HttpClientBuilder;
import br.com.rcaneppele.openai.http.HttpRequestBuilder;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.time.Duration;

public class OpenAIClient {

    private final String apiKey;
    private final OkHttpClient httpClient;
    private final JsonConverter jsonConverter;

    public OpenAIClient(String apiKey, int timeoutInSeconds) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("API Key is required!");
        }

        this.apiKey = apiKey;
        this.httpClient = new HttpClientBuilder().build(Duration.ofSeconds(timeoutInSeconds));
        this.jsonConverter = new JsonConverter();
    }

    public OpenAIClient(String apiKey) {
        this(apiKey, 15);
    }

    public ChatCompletionResponse sendChatCompletionRequest(ChatCompletionRequest request) {
        var chatCompletionUri = "chat/completions";
        var requestJson = jsonConverter.convertChatCompletionRequestToJson(request);
        var httpRequest = new HttpRequestBuilder().buildPost(chatCompletionUri, this.apiKey, requestJson);

        try {
            var response = httpClient.newCall(httpRequest).execute();
            return new ChatCompletionResponseBuilder().build(response);
        } catch (IOException e) {
            throw new RuntimeException("Error sending chat completion request", e);
        }
    }

}
