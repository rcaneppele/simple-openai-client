package br.com.rcaneppele.openai;

import br.com.rcaneppele.openai.chatcompletion.request.ChatCompletionRequest;
import br.com.rcaneppele.openai.chatcompletion.request.ChatCompletionRequestSender;

import java.time.Duration;

public class OpenAIClient {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(15);
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/";

    private final String apiKey;
    private final Duration timeout;

    public OpenAIClient(String apiKey, Duration timeout) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("API Key is required!");
        }

        this.apiKey = apiKey;
        this.timeout = timeout != null ? timeout : DEFAULT_TIMEOUT;
    }

    public OpenAIClient(String apiKey) {
        this(apiKey, DEFAULT_TIMEOUT);
    }

    public String sendChatCompletionRequest(ChatCompletionRequest request) {
        var sender = new ChatCompletionRequestSender(OPENAI_API_URL, timeout, apiKey);
        return sender.sendRequest(request);
    }

}
