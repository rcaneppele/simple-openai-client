package br.com.rcaneppele.openai.endpoints.message.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.endpoints.message.response.Message;

import java.time.Duration;

public class RetrieveMessageRequestSender extends MessageRequestSender<Void, Message> {

    private final String messageId;

    public RetrieveMessageRequestSender(String apiBaseUrl, Duration timeout, String apiKey, String threadId, String messageId) {
        super(apiBaseUrl, timeout, apiKey, threadId);
        this.messageId = messageId;
    }

    @Override
    protected String endpointUri() {
        return super.endpointUri() + "/" +this.messageId;
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
    protected Class<Message> responseType() {
        return Message.class;
    }

}
