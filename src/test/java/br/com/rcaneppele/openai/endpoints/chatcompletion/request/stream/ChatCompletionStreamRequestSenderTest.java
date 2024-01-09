package br.com.rcaneppele.openai.endpoints.chatcompletion.request.stream;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.chatcompletion.request.ChatCompletionRequest;
import br.com.rcaneppele.openai.endpoints.chatcompletion.request.ChatCompletionRequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ChatCompletionStreamRequestSenderTest extends BaseRequestSenderTest {

    private ChatCompletionStreamRequestSender sender;
    private JsonConverter<ChatCompletionRequest> jsonConverter;
    private ChatCompletionRequestBuilder builder;

    @Override
    protected String expectedURI() {
        return "chat/completions";
    }

    @Override
    protected String mockJsonResponse() {
        return "data: {}";
    }

    @BeforeEach
    void setUp() {
        this.sender = new ChatCompletionStreamRequestSender(url, TIMEOUT, API_KEY);
        this.jsonConverter = new JsonConverter<>(ChatCompletionRequest.class);
        this.builder = new ChatCompletionRequestBuilder();
    }

    @Test
    public void shouldSendRequest() {
        var request = builder
                .model(OpenAIModel.GPT_4_1106_PREVIEW)
                .userMessage("the user message")
                .build();

        var testObserver = sender.sendStreamRequest(request).test();
        testObserver.assertNoErrors();
        testObserver.assertNotComplete();
        assertNotNull(testObserver.values());

        var expectedRequestBody = jsonConverter.convertRequestToJson(request);
        executeRequestAssertions(expectedRequestBody, 1, HttpMethod.POST, false);
    }

}
