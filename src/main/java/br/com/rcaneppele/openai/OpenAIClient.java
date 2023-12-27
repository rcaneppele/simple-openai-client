package br.com.rcaneppele.openai;

import br.com.rcaneppele.openai.chatcompletion.request.ChatCompletionRequest;
import br.com.rcaneppele.openai.chatcompletion.request.ChatCompletionRequestSender;
import br.com.rcaneppele.openai.chatcompletion.response.ChatCompletionResponse;

import java.time.Duration;

public class OpenAIClient {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/";

    private final String apiKey;
    private final Duration timeout;

    public OpenAIClient(String apiKey, int timeoutInSeconds) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("API Key is required!");
        }

        this.apiKey = apiKey;
        this.timeout = Duration.ofSeconds(timeoutInSeconds);
    }

    public OpenAIClient(String apiKey) {
        this(apiKey, 15);
    }

    public ChatCompletionResponse sendChatCompletionRequest(ChatCompletionRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request object is required!");
        }

        var sender = new ChatCompletionRequestSender(OPENAI_API_URL, timeout, apiKey);
        return sender.sendRequest(request);
    }

}
