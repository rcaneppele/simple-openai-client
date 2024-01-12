package br.com.rcaneppele.openai.endpoints.chatcompletion.request;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.common.message.ChatCompletionChoice;
import br.com.rcaneppele.openai.common.message.ChatMessage;
import br.com.rcaneppele.openai.common.message.TokenUsage;
import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.chatcompletion.response.ChatCompletion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

class ChatCompletionRequestSenderTest extends BaseRequestSenderTest {

    private RequestSender<ChatCompletionRequest, ChatCompletion> sender;
    private JsonConverter<ChatCompletionRequest> jsonConverter;
    private ChatCompletionRequestBuilder builder;

    @Override
    protected String expectedURI() {
        return "chat/completions";
    }

    @Override
    protected String mockJsonResponse() {
        return """
                {
                  "id": "chatcmpl-123",
                  "object": "chat.completion",
                  "created": 1677652288,
                  "model": "gpt-3.5-turbo",
                  "system_fingerprint": "fp_44709d6fcb",
                  "choices": [{
                    "index": 0,
                    "message": {
                      "role": "assistant",
                      "content": "Hello there, how may I assist you today?"
                    },
                    "logprobs": null,
                    "finish_reason": "stop"
                  }],
                  "usage": {
                    "prompt_tokens": 9,
                    "completion_tokens": 12,
                    "total_tokens": 21
                  }
                }
                """;
    }

    @BeforeEach
    void setUp() {
        this.sender = new ChatCompletionRequestSender(url, TIMEOUT, API_KEY);
        this.jsonConverter = new JsonConverter<>(ChatCompletionRequest.class);
        this.builder = new ChatCompletionRequestBuilder();
    }

    @Test
    public void shouldSendRequest() {
        var request = builder
                .model(OpenAIModel.GPT_4_1106_PREVIEW)
                .userMessage("the user message")
                .build();

        var actualResponse = sender.sendRequest(request);
        var expectedResponse = new ChatCompletion(
                "chatcmpl-123",
                "chat.completion",
                Instant.ofEpochSecond(1677652288),
                OpenAIModel.GPT_3_5_TURBO,
                "fp_44709d6fcb",
                List.of(
                        new ChatCompletionChoice(
                                0l,
                                new ChatMessage("assistant", "Hello there, how may I assist you today?"),
                                null,
                                "stop"
                        )
                ),
                new TokenUsage(9, 12 ,21)
        );
        var expectedRequestBody = jsonConverter.convertRequestToJson(request);
        executeRequestAssertions(expectedRequestBody, 1, HttpMethod.POST, false);
        executeResponseAssertions(expectedResponse, actualResponse);
    }

}
