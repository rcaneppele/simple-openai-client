package br.com.rcaneppele.openai.endpoints.assistant.request;

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

class CreateAssistantRequestSenderTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(2);
    private static final String API_KEY = "fake-api-key";
    private static final String TEST_API_URL = "/test/";
    private static final String CHAT_COMPLETION_URL = TEST_API_URL +"assistants";
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static final String AUTH_HEADER = "Bearer " +API_KEY;
    private static final String ASSISTANT_HEADER = "assistants=v1";

    private MockWebServer server;
    private CreateAssistantRequestSender sender;

    @BeforeEach
    void setUp() throws IOException {
        this.server = new MockWebServer();
        this.server.start();
        var url = this.server.url(TEST_API_URL).url().toString();
        this.sender = new CreateAssistantRequestSender(url, TIMEOUT, API_KEY);
    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    public void shouldSendCreateAssistantRequest() throws InterruptedException {
        var mockHttpResponse = new MockResponse()
                .setBody("{}}")
                .setResponseCode(200);

        server.enqueue(mockHttpResponse);

        var request = new CreateAssistantRequestBuilder()
                .model(OpenAIModel.GPT_4_1106_PREVIEW)
                .name("Test assistant")
                .description("Description")
                .description("Instructions")
                .retrieval()
                .fileIds("file-123")
                .build();

        var response = sender.sendRequest(request);

        assertNotNull(response);

        var httpRequest = server.takeRequest();
        assertEquals(1, server.getRequestCount());
        assertEquals("POST", httpRequest.getMethod());
        assertEquals(CHAT_COMPLETION_URL, httpRequest.getPath());
        assertEquals(MEDIA_TYPE.toString(), httpRequest.getHeader("Content-Type"));
        assertEquals(AUTH_HEADER, httpRequest.getHeader("Authorization"));
        assertEquals(ASSISTANT_HEADER, httpRequest.getHeader("OpenAI-Beta"));
        var actualHttpRequestBody = httpRequest.getBody().readUtf8();
        var expectedHttpRequestBody = new JsonConverter<CreateAssistantRequest>(CreateAssistantRequest.class).convertRequestToJson(request);
        assertEquals(expectedHttpRequestBody, actualHttpRequestBody);
    }

}