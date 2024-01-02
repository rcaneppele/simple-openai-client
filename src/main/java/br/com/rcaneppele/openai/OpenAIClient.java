package br.com.rcaneppele.openai;

import br.com.rcaneppele.openai.endpoints.assistant.request.CreateAssistantFileRequest;
import br.com.rcaneppele.openai.endpoints.assistant.request.CreateAssistantRequest;
import br.com.rcaneppele.openai.endpoints.assistant.request.ListAssistantFilesRequest;
import br.com.rcaneppele.openai.endpoints.assistant.request.ListAssistantsRequest;
import br.com.rcaneppele.openai.endpoints.assistant.request.sender.CreateAssistantFileRequestSender;
import br.com.rcaneppele.openai.endpoints.assistant.request.sender.CreateAssistantRequestSender;
import br.com.rcaneppele.openai.endpoints.assistant.request.sender.ListAssistantFilesRequestSender;
import br.com.rcaneppele.openai.endpoints.assistant.request.sender.ListAssistantsRequestSender;
import br.com.rcaneppele.openai.endpoints.assistant.response.Assistant;
import br.com.rcaneppele.openai.endpoints.assistant.response.AssistantFile;
import br.com.rcaneppele.openai.endpoints.assistant.response.ListOfAssistantFiles;
import br.com.rcaneppele.openai.endpoints.assistant.response.ListOfAssistants;
import br.com.rcaneppele.openai.endpoints.chatcompletion.request.ChatCompletionRequest;
import br.com.rcaneppele.openai.endpoints.chatcompletion.request.ChatCompletionRequestSender;
import br.com.rcaneppele.openai.endpoints.chatcompletion.request.stream.ChatCompletionStreamRequestSender;
import br.com.rcaneppele.openai.endpoints.chatcompletion.response.ChatCompletion;
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

    public ChatCompletion sendChatCompletionRequest(ChatCompletionRequest request) {
        var sender = new ChatCompletionRequestSender(OPENAI_API_URL, timeout, apiKey);
        return sender.sendRequest(request);
    }

    public Observable<ChatCompletion> sendStreamChatCompletionRequest(ChatCompletionRequest request) {
        request = request.withStream();
        var sender = new ChatCompletionStreamRequestSender(OPENAI_API_URL, timeout, apiKey);
        return sender.sendStreamRequest(request);
    }

    public Assistant sendCreateAssistantRequest(CreateAssistantRequest request) {
        var sender = new CreateAssistantRequestSender(OPENAI_API_URL, timeout, apiKey);
        return sender.sendRequest(request);
    }

    public AssistantFile sendCreateAssistantFileRequest(CreateAssistantFileRequest request) {
        var assistantId = request.assistantId();
        var sender = new CreateAssistantFileRequestSender(OPENAI_API_URL, timeout, apiKey, assistantId);
        return sender.sendRequest(request);
    }

    public ListOfAssistants sendListAssistantsRequest(ListAssistantsRequest request) {
        var sender = new ListAssistantsRequestSender(OPENAI_API_URL, timeout, apiKey);
        return sender.sendRequest(request);
    }

    public ListOfAssistantFiles sendListAssistantFilesRequest(ListAssistantFilesRequest request) {
        var assistantId = request.assistantId();
        var sender = new ListAssistantFilesRequestSender(OPENAI_API_URL, timeout, apiKey, assistantId);
        return sender.sendRequest(request);
    }

    private void validateApiKey(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("API Key is required!");
        }
    }

}
