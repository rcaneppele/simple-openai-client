package br.com.rcaneppele.openai.endpoints.message.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.endpoints.message.response.MessageFile;

import java.time.Duration;

public class RetrieveMessageFileRequestSender extends MessageRequestSender<Void, MessageFile> {

    private final String messageId;
    private final String fileId;

    public RetrieveMessageFileRequestSender(String apiBaseUrl, Duration timeout, String apiKey, String threadId, String messageId, String fileId) {
        super(apiBaseUrl, timeout, apiKey, threadId);
        this.messageId = messageId;
        this.fileId = fileId;
    }

    @Override
    protected String endpointUri() {
        return super.endpointUri() + "/" +this.messageId +"/files/" +this.fileId;
    }

    @Override
    protected HttpMethod httpMethod() {
        return HttpMethod.GET;
    }

    @Override
    protected Class<Void> requestType() {
        return Void.class;
    }

    @Override
    protected Class<MessageFile> responseType() {
        return MessageFile.class;
    }

}
