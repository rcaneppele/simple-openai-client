package br.com.rcaneppele.openai.endpoints.chatcompletion.request;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.endpoints.chatcompletion.response.ChatCompletion;

import java.time.Duration;

public class ChatCompletionRequestSender extends RequestSender<ChatCompletionRequest, ChatCompletion> {

    public ChatCompletionRequestSender(String apiBaseUrl, Duration timeout, String apiKey) {
        super(apiBaseUrl, timeout, apiKey);
    }

    @Override
    protected String endpointUri() {
        return "chat/completions";
    }

    @Override
    protected HttpMethod httpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected Class<ChatCompletionRequest> requestType() {
        return ChatCompletionRequest.class;
    }

    @Override
    protected Class<ChatCompletion> responseType() {
        return ChatCompletion.class;
    }

}
