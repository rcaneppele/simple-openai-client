package br.com.rcaneppele.openai;

import br.com.rcaneppele.openai.chatcompletion.request.ChatCompletionRequest;
import br.com.rcaneppele.openai.chatcompletion.request.ChatCompletionRequestSender;
import br.com.rcaneppele.openai.chatcompletion.request.stream.ChatCompletionStreamRequestSender;
import br.com.rcaneppele.openai.chatcompletion.response.ChatCompletionResponse;
import br.com.rcaneppele.openai.endpoints.assistant.assistant.request.CreateAssistantRequest;
import br.com.rcaneppele.openai.endpoints.assistant.assistant.request.CreateAssistantRequestSender;
import br.com.rcaneppele.openai.endpoints.assistant.assistant.response.CreateAssistantResponse;
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
        var sender = new ChatCompletionRequestSender(OPENAI_API_URL, timeout, apiKey);
        return sender.sendRequest(request);
    }

    public Observable<ChatCompletionResponse> sendStreamChatCompletionRequest(ChatCompletionRequest request) {
        request = request.withStream();
        var sender = new ChatCompletionStreamRequestSender(OPENAI_API_URL, timeout, apiKey);
        return sender.sendStreamRequest(request);
    }

    public CreateAssistantResponse sendCreateAssistantRequest(CreateAssistantRequest request) {
        var sender = new CreateAssistantRequestSender(OPENAI_API_URL, timeout, apiKey);
        return sender.sendRequest(request);
    }

    private void validateApiKey(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("API Key is required!");
        }
    }

}
