package br.com.rcaneppele.openai;

import br.com.rcaneppele.openai.chatcompletion.request.ChatCompletionRequest;
import br.com.rcaneppele.openai.chatcompletion.request.ChatCompletionRequestSender;
import br.com.rcaneppele.openai.chatcompletion.response.ChatCompletionResponse;
import io.reactivex.rxjava3.core.Observable;

import java.time.Duration;

public class OpenAIClient {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/";

    private final String apiKey;
    private final Duration timeout;

    public OpenAIClient(String apiKey, int timeoutInSeconds) {
        validateApiKey(apiKey);
        this.apiKey = apiKey;
        this.timeout = Duration.ofSeconds(timeoutInSeconds);
    }

    public OpenAIClient(String apiKey) {
        this(apiKey, 15);
    }

    public ChatCompletionResponse sendChatCompletionRequest(ChatCompletionRequest request) {
        validateChatCompletionRequest(request);
        var sender = new ChatCompletionRequestSender(OPENAI_API_URL, timeout, apiKey);
        return sender.sendRequest(request);
    }

    public Observable<ChatCompletionResponse> sendStreamChatCompletionRequest(ChatCompletionRequest request) {
        validateChatCompletionRequest(request);
        request = request.withStream();
        var sender = new ChatCompletionRequestSender(OPENAI_API_URL, timeout, apiKey);
        return sender.sendStreamRequest(request);
    }

    private void validateApiKey(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("API Key is required!");
        }
    }

    private void validateChatCompletionRequest(ChatCompletionRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request object is required!");
        }
    }

}
