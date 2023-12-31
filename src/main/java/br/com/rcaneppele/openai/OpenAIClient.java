package br.com.rcaneppele.openai;

import br.com.rcaneppele.openai.common.request.QueryParameters;
import br.com.rcaneppele.openai.common.response.DeletionStatus;
import br.com.rcaneppele.openai.common.validation.IdValidator;
import br.com.rcaneppele.openai.endpoints.assistant.request.CreateAssistantFileRequest;
import br.com.rcaneppele.openai.endpoints.assistant.request.CreateAssistantRequest;
import br.com.rcaneppele.openai.endpoints.assistant.request.ModifyAssistantRequest;
import br.com.rcaneppele.openai.endpoints.assistant.request.sender.*;
import br.com.rcaneppele.openai.endpoints.assistant.response.Assistant;
import br.com.rcaneppele.openai.endpoints.assistant.response.AssistantFile;
import br.com.rcaneppele.openai.endpoints.assistant.response.ListOfAssistantFiles;
import br.com.rcaneppele.openai.endpoints.assistant.response.ListOfAssistants;
import br.com.rcaneppele.openai.endpoints.chatcompletion.request.ChatCompletionRequest;
import br.com.rcaneppele.openai.endpoints.chatcompletion.request.ChatCompletionRequestSender;
import br.com.rcaneppele.openai.endpoints.chatcompletion.request.stream.ChatCompletionStreamRequestSender;
import br.com.rcaneppele.openai.endpoints.chatcompletion.response.ChatCompletion;
import br.com.rcaneppele.openai.endpoints.threads.request.CreateThreadRequest;
import br.com.rcaneppele.openai.endpoints.threads.request.ModifyThreadRequest;
import br.com.rcaneppele.openai.endpoints.threads.request.builder.CreateThreadRequestBuilder;
import br.com.rcaneppele.openai.endpoints.threads.request.sender.CreateThreadRequestSender;
import br.com.rcaneppele.openai.endpoints.threads.request.sender.DeleteThreadRequestSender;
import br.com.rcaneppele.openai.endpoints.threads.request.sender.ModifyThreadRequestSender;
import br.com.rcaneppele.openai.endpoints.threads.request.sender.RetrieveThreadRequestSender;
import br.com.rcaneppele.openai.endpoints.threads.response.Thread;
import io.reactivex.rxjava3.core.Observable;

import java.time.Duration;

public class OpenAIClient {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/";

    private final String apiKey;
    private final Duration timeout;
    private final IdValidator idValidator;

    public OpenAIClient(String apiKey, int timeoutInSeconds) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("API Key is required!");
        }

        this.apiKey = apiKey;
        this.timeout = Duration.ofSeconds(timeoutInSeconds);
        this.idValidator = new IdValidator();
    }

    public OpenAIClient(String apiKey) {
        this(apiKey, 15);
    }

    public ChatCompletion chatCompletion(ChatCompletionRequest request) {
        var sender = new ChatCompletionRequestSender(OPENAI_API_URL, timeout, apiKey);
        return sender.sendRequest(request);
    }

    public Observable<ChatCompletion> streamChatCompletion(ChatCompletionRequest request) {
        request = request.withStream();
        var sender = new ChatCompletionStreamRequestSender(OPENAI_API_URL, timeout, apiKey);
        return sender.sendStreamRequest(request);
    }

    public Assistant createAssistant(CreateAssistantRequest request) {
        var sender = new CreateAssistantRequestSender(OPENAI_API_URL, timeout, apiKey);
        return sender.sendRequest(request);
    }

    public AssistantFile createAssistantFile(CreateAssistantFileRequest request) {
        var assistantId = request.assistantId();
        idValidator.validateAssistantId(assistantId);
        var sender = new CreateAssistantFileRequestSender(OPENAI_API_URL, timeout, apiKey, assistantId);
        return sender.sendRequest(request);
    }

    public ListOfAssistants listAssistants(QueryParameters parameters) {
        var sender = new ListAssistantsRequestSender(OPENAI_API_URL, timeout, apiKey);
        return sender.sendRequest(parameters);
    }

    public ListOfAssistants listAssistants() {
        return this.listAssistants(null);
    }

    public ListOfAssistantFiles listAssistantFiles(String assistantId, QueryParameters parameters) {
        idValidator.validateAssistantId(assistantId);
        var sender = new ListAssistantFilesRequestSender(OPENAI_API_URL, timeout, apiKey, assistantId);
        return sender.sendRequest(parameters);
    }

    public ListOfAssistantFiles listAssistantFiles(String assistantId) {
        return this.listAssistantFiles(assistantId, null);
    }

    public Assistant retrieveAssistant(String assistantId) {
        idValidator.validateAssistantId(assistantId);
        var sender = new RetrieveAssistantRequestSender(OPENAI_API_URL, timeout, apiKey, assistantId);
        return sender.sendRequest(null);
    }

    public AssistantFile retrieveAssistantFile(String assistantId, String fileId) {
        idValidator.validateAssistantId(assistantId);
        idValidator.validateFileId(fileId);

        var sender = new RetrieveAssistantFileRequestSender(OPENAI_API_URL, timeout, apiKey, assistantId, fileId);
        return sender.sendRequest(null);
    }

    public Assistant modifyAssistant(ModifyAssistantRequest request) {
        var assistantId = request.assistantId();
        idValidator.validateAssistantId(assistantId);
        var sender = new ModifyAssistantRequestSender(OPENAI_API_URL, timeout, apiKey, assistantId);
        return sender.sendRequest(request);
    }

    public DeletionStatus deleteAssistant(String assistantId) {
        idValidator.validateAssistantId(assistantId);
        var sender = new DeleteAssistantRequestSender(OPENAI_API_URL, timeout, apiKey, assistantId);
        return sender.sendRequest(null);
    }

    public DeletionStatus deleteAssistantFile(String assistantId, String fileId) {
        idValidator.validateAssistantId(assistantId);
        idValidator.validateFileId(fileId);

        var sender = new DeleteAssistantFileRequestSender(OPENAI_API_URL, timeout, apiKey, assistantId, fileId);
        return sender.sendRequest(null);
    }

    public Thread createThread(CreateThreadRequest request) {
        var sender = new CreateThreadRequestSender(OPENAI_API_URL, timeout, apiKey);
        return sender.sendRequest(request);
    }

    public Thread createThread() {
        var request = new CreateThreadRequestBuilder().build();
        return this.createThread(request);
    }

    public Thread retrieveThread(String threadId) {
        idValidator.validateThreadId(threadId);
        var sender = new RetrieveThreadRequestSender(OPENAI_API_URL, timeout, apiKey, threadId);
        return sender.sendRequest(null);
    }

    public Thread modifyThread(ModifyThreadRequest request) {
        var threadId = request.threadId();
        idValidator.validateThreadId(threadId);
        var sender = new ModifyThreadRequestSender(OPENAI_API_URL, timeout, apiKey, threadId);
        return sender.sendRequest(request);
    }

    public DeletionStatus deleteThread(String threadId) {
        idValidator.validateThreadId(threadId);
        var sender = new DeleteThreadRequestSender(OPENAI_API_URL, timeout, apiKey, threadId);
        return sender.sendRequest(null);
    }

}
