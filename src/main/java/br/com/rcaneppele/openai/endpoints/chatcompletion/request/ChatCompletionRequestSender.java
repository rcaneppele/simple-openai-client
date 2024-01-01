package br.com.rcaneppele.openai.chatcompletion.request;

import br.com.rcaneppele.openai.chatcompletion.response.ChatCompletionResponse;
import br.com.rcaneppele.openai.common.request.RequestSender;

import java.time.Duration;

public class ChatCompletionRequestSender extends RequestSender<ChatCompletionRequest, ChatCompletionResponse> {

    public ChatCompletionRequestSender(String apiBaseUrl, Duration timeout, String apiKey) {
        super(apiBaseUrl, timeout, apiKey);
    }

    @Override
    protected String endpointUri() {
        return "chat/completions";
    }

    @Override
    protected Class<ChatCompletionRequest> requestType() {
        return ChatCompletionRequest.class;
    }

    @Override
    protected Class<ChatCompletionResponse> responseType() {
        return ChatCompletionResponse.class;
    }

}
