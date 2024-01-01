package br.com.rcaneppele.openai.endpoints.chatcompletion.request.stream;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.chatcompletion.request.ChatCompletionRequest;
import br.com.rcaneppele.openai.endpoints.chatcompletion.request.ChatCompletionRequestBuilder;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ChatCompletionStreamRequestSenderTest extends BaseRequestSenderTest {

    private ChatCompletionStreamRequestSender sender;
    private JsonConverter<ChatCompletionRequest> jsonConverter;

    @Override
    protected String expectedURI() {
        return "chat/completions";
    }

    @Override
    protected MockResponse mockResponse() {
        return new MockResponse()
                .setHeader("Content-Type", "text/event-stream")
                .setBody("data: {}");
    }

    @BeforeEach
    void setUp() {
        this.sender = new ChatCompletionStreamRequestSender(url, TIMEOUT, API_KEY);
        this.jsonConverter = new JsonConverter<>(ChatCompletionRequest.class);
    }

    @Test
    public void shouldSendStreamChatCompletionRequest() throws InterruptedException {
        var request = new ChatCompletionRequestBuilder()
                .model(OpenAIModel.GPT_4_1106_PREVIEW)
                .userMessage("the user message")
                .build();

        var testObserver = sender.sendStreamRequest(request).test();
        testObserver.assertNoErrors();
        testObserver.assertNotComplete();
        assertNotNull(testObserver.values());

        var httpRequest = server.takeRequest();
        var expectedRequestBody = jsonConverter.convertRequestToJson(request);
        executeCommonAssertions(httpRequest, expectedRequestBody, 1, "POST");
        assertEquals("text/event-stream", httpRequest.getHeader("Accept"));
    }

}
