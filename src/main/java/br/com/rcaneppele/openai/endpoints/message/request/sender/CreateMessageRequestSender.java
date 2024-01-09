package br.com.rcaneppele.openai.endpoints.message.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.endpoints.message.request.CreateMessageRequest;
import br.com.rcaneppele.openai.endpoints.message.response.Message;

import java.time.Duration;

public class CreateMessageRequestSender extends MessageRequestSender<CreateMessageRequest, Message> {

    public CreateMessageRequestSender(String apiBaseUrl, Duration timeout, String apiKey, String threadId) {
        super(apiBaseUrl, timeout, apiKey, threadId);
    }

    @Override
    protected HttpMethod httpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected Class<CreateMessageRequest> requestType() {
        return CreateMessageRequest.class;
    }

    @Override
    protected Class<Message> responseType() {
        return Message.class;
    }

}
