package br.com.rcaneppele.openai.endpoints.message.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.endpoints.message.request.ModifyMessageRequest;
import br.com.rcaneppele.openai.endpoints.message.response.Message;

import java.time.Duration;

public class ModifyMessageRequestSender extends MessageRequestSender<ModifyMessageRequest, Message> {

    private final String messageId;

    public ModifyMessageRequestSender(String apiBaseUrl, Duration timeout, String apiKey, String threadId, String messageId) {
        super(apiBaseUrl, timeout, apiKey, threadId);
        this.messageId = messageId;
    }

    @Override
    protected String endpointUri() {
        return super.endpointUri() + "/" +this.messageId;
    }

    @Override
    protected HttpMethod httpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected Class<ModifyMessageRequest> requestType() {
        return ModifyMessageRequest.class;
    }

    @Override
    protected Class<Message> responseType() {
        return Message.class;
    }

}
