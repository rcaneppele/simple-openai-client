package br.com.rcaneppele.openai.chatcompletion.request;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.common.json.JsonConverter;
import okhttp3.MediaType;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ChatCompletionRequestSenderTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(2);
    private static final String API_KEY = "fake-api-key";
    private static final String TEST_API_URL = "/test/";
    private static final String CHAT_COMPLETION_URL = TEST_API_URL +"chat/completions";
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static final String AUTH_HEADER = "Bearer " +API_KEY;

    private MockWebServer server;
    private ChatCompletionRequestSender sender;
    private JsonConverter<ChatCompletionRequest> jsonConverter;

    @BeforeEach
    void setUp() throws IOException {
        this.server = new MockWebServer();
        this.server.start();
        var url = this.server.url(TEST_API_URL).url().toString();
        this.sender = new ChatCompletionRequestSender(url, TIMEOUT, API_KEY);
        this.jsonConverter = new JsonConverter<>(ChatCompletionRequest.class);
    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    public void shouldSendChatCompletionRequest() throws InterruptedException {
        var mockHttpResponse = new MockResponse()
                .setBody("{}}")
                .setResponseCode(200);

        server.enqueue(mockHttpResponse);

        var chatCompletionRequest = new ChatCompletionRequestBuilder()
                .model(OpenAIModel.GPT_4_1106_PREVIEW)
                .userMessage("the user message")
                .build();

        var response = sender.sendRequest(chatCompletionRequest);

        assertNotNull(response);

        var httpRequest = server.takeRequest();
        assertEquals(1, server.getRequestCount());
        assertEquals("POST", httpRequest.getMethod());
        assertEquals(CHAT_COMPLETION_URL, httpRequest.getPath());
        assertEquals(MEDIA_TYPE.toString(), httpRequest.getHeader("Content-Type"));
        assertEquals(AUTH_HEADER, httpRequest.getHeader("Authorization"));
        var actualHttpRequestBody = httpRequest.getBody().readUtf8();
        var expectedHttpRequestBody = jsonConverter.convertRequestToJson(chatCompletionRequest);
        assertEquals(expectedHttpRequestBody, actualHttpRequestBody);
    }

}
