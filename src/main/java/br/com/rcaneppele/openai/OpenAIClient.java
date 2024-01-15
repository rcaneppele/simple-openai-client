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
import br.com.rcaneppele.openai.endpoints.message.request.CreateMessageRequest;
import br.com.rcaneppele.openai.endpoints.message.request.ModifyMessageRequest;
import br.com.rcaneppele.openai.endpoints.message.request.sender.*;
import br.com.rcaneppele.openai.endpoints.message.response.ListOfMessageFiles;
import br.com.rcaneppele.openai.endpoints.message.response.ListOfMessages;
import br.com.rcaneppele.openai.endpoints.message.response.Message;
import br.com.rcaneppele.openai.endpoints.message.response.MessageFile;
import br.com.rcaneppele.openai.endpoints.run.request.CreateRunRequest;
import br.com.rcaneppele.openai.endpoints.run.request.sender.CreateRunRequestSender;
import br.com.rcaneppele.openai.endpoints.run.request.sender.ListRunsRequestSender;
import br.com.rcaneppele.openai.endpoints.run.request.sender.RetrieveRunRequestSender;
import br.com.rcaneppele.openai.endpoints.run.response.ListOfRuns;
import br.com.rcaneppele.openai.endpoints.run.response.Run;
import br.com.rcaneppele.openai.endpoints.thread.request.CreateThreadRequest;
import br.com.rcaneppele.openai.endpoints.thread.request.ModifyThreadRequest;
import br.com.rcaneppele.openai.endpoints.thread.request.builder.CreateThreadRequestBuilder;
import br.com.rcaneppele.openai.endpoints.thread.request.sender.CreateThreadRequestSender;
import br.com.rcaneppele.openai.endpoints.thread.request.sender.DeleteThreadRequestSender;
import br.com.rcaneppele.openai.endpoints.thread.request.sender.ModifyThreadRequestSender;
import br.com.rcaneppele.openai.endpoints.thread.request.sender.RetrieveThreadRequestSender;
import br.com.rcaneppele.openai.endpoints.thread.response.Thread;
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

    public Message createMessage(CreateMessageRequest request) {
        var threadId = request.threadId();
        idValidator.validateThreadId(threadId);
        var sender = new CreateMessageRequestSender(OPENAI_API_URL, timeout, apiKey, threadId);
        return sender.sendRequest(request);
    }

    public ListOfMessages listMessages(String threadId, QueryParameters parameters) {
        idValidator.validateThreadId(threadId);
        var sender = new ListMessagesRequestSender(OPENAI_API_URL, timeout, apiKey, threadId);
        return sender.sendRequest(parameters);
    }

    public ListOfMessages listMessages(String threadId) {
        return this.listMessages(threadId, null);
    }

    public ListOfMessageFiles listMessageFiles(String threadId, String messageId, QueryParameters parameters) {
        idValidator.validateThreadId(threadId);
        idValidator.validateMessageId(messageId);
        var sender = new ListMessageFilesRequestSender(OPENAI_API_URL, timeout, apiKey, threadId, messageId);
        return sender.sendRequest(parameters);
    }

    public ListOfMessageFiles listMessageFiles(String threadId, String messageId) {
        return this.listMessageFiles(threadId, messageId, null);
    }

    public Message retrieveMessage(String threadId, String messageId) {
        idValidator.validateThreadId(threadId);
        idValidator.validateMessageId(messageId);
        var sender = new RetrieveMessageRequestSender(OPENAI_API_URL, timeout, apiKey, threadId, messageId);
        return sender.sendRequest(null);
    }

    public MessageFile retrieveMessageFile(String threadId, String messageId, String fileId) {
        idValidator.validateThreadId(threadId);
        idValidator.validateMessageId(messageId);
        idValidator.validateFileId(fileId);
        var sender = new RetrieveMessageFileRequestSender(OPENAI_API_URL, timeout, apiKey, threadId, messageId, fileId);
        return sender.sendRequest(null);
    }

    public Message modifyMessage(ModifyMessageRequest request) {
        var threadId = request.threadId();
        var messageId = request.messageId();
        idValidator.validateThreadId(threadId);
        idValidator.validateMessageId(messageId);
        var sender = new ModifyMessageRequestSender(OPENAI_API_URL, timeout, apiKey, threadId, messageId);
        return sender.sendRequest(request);
    }

    public Run createRun(CreateRunRequest request) {
        var threadId = request.threadId();
        var assistantId = request.assistantId();
        idValidator.validateThreadId(threadId);
        idValidator.validateAssistantId(assistantId);
        var sender = new CreateRunRequestSender(OPENAI_API_URL, timeout, apiKey, threadId);
        return sender.sendRequest(request);
    }

    public ListOfRuns listRuns(String threadId, QueryParameters parameters) {
        idValidator.validateThreadId(threadId);
        var sender = new ListRunsRequestSender(OPENAI_API_URL, timeout, apiKey, threadId);
        return sender.sendRequest(parameters);
    }

    public ListOfRuns listRuns(String threadId) {
        return this.listRuns(threadId, null);
    }

    public Run retrieveRun(String threadId, String runId) {
        idValidator.validateThreadId(threadId);
        idValidator.validateRunId(runId);
        var sender = new RetrieveRunRequestSender(OPENAI_API_URL, timeout, apiKey, threadId, runId);
        return sender.sendRequest(null);
    }

}
