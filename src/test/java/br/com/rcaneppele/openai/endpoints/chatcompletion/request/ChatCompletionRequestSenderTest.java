package br.com.rcaneppele.openai.endpoints.chatcompletion.request;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.chatcompletion.response.ChatCompletion;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ChatCompletionRequestSenderTest extends BaseRequestSenderTest {

    private RequestSender<ChatCompletionRequest, ChatCompletion> sender;
    private JsonConverter<ChatCompletionRequest> jsonConverter;

    @Override
    protected String expectedURI() {
        return "chat/completions";
    }

    @Override
    protected MockResponse mockResponse() {
        return new MockResponse()
                .setResponseCode(200)
                .setBody("""
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
                        """);
    }

    @BeforeEach
    void setUp() {
        this.sender = new ChatCompletionRequestSender(url, TIMEOUT, API_KEY);
        this.jsonConverter = new JsonConverter<>(ChatCompletionRequest.class);
    }

    @Test
    public void shouldSendChatCompletionRequest() throws InterruptedException {
        var request = new ChatCompletionRequestBuilder()
                .model(OpenAIModel.GPT_4_1106_PREVIEW)
                .userMessage("the user message")
                .build();

        var response = sender.sendRequest(request);
        var httpRequest = server.takeRequest();
        var expectedRequestBody = jsonConverter.convertRequestToJson(request);
        executeCommonAssertions(httpRequest, expectedRequestBody, 1, "POST");

        assertNotNull(response);
        assertEquals("chatcmpl-123", response.id());
        assertEquals("chat.completion", response.object());
        assertEquals(Instant.ofEpochSecond(1677652288), response.created());
        assertEquals(OpenAIModel.GPT_3_5_TURBO.getName(), response.model());
        assertEquals("fp_44709d6fcb", response.systemFingerprint());
        assertEquals(1, response.choices().size());
        assertEquals("Hello there, how may I assist you today?", response.firstChoiceMessageContent());
        assertEquals("assistant", response.choices().get(0).message().role());
        assertNull(response.choices().get(0).logProbs());
        assertEquals("stop", response.choices().get(0).finishReason());
        assertEquals(9, response.usage().promptTokens());
        assertEquals(12, response.usage().completionTokens());
        assertEquals(21, response.usage().totalTokens());
    }

}
