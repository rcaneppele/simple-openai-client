package br.com.rcaneppele.openai.endpoints;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import okhttp3.MediaType;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class BaseRequestSenderTest {

    protected static final Duration TIMEOUT = Duration.ofSeconds(2);
    protected static final String API_KEY = "fake-api-key";
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static final String AUTH_HEADER_VALUE = "Bearer " +API_KEY;
    private static final String ASSISTANT_HEADER_VALUE = "assistants=v1";
    private static final String TEST_BASE_URL = "/test/";

    protected MockWebServer server;
    protected String url;

    @BeforeEach
    void baseSetUp() throws IOException {
        this.server = new MockWebServer();
        this.server.start();
        this.url = this.server.url(TEST_BASE_URL).url().toString();
        this.server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockJsonResponse()));
    }

    @AfterEach
    void baseTearDown() throws IOException {
        server.shutdown();
    }

    public void executeRequestAssertions(String expectedRequestBody, int expectedRequestCount, HttpMethod expectedHttpMethod, boolean assertAssistantHeader) {
        try {
            var request = server.takeRequest();
            assertEquals(expectedRequestCount, server.getRequestCount());
            assertEquals(expectedHttpMethod.toString(), request.getMethod());
            assertEquals(TEST_BASE_URL + expectedURI(), request.getPath());
            assertEquals(MEDIA_TYPE.toString(), request.getHeader("Content-Type"));
            assertEquals(expectedRequestBody, request.getBody().readUtf8());
            assertEquals(AUTH_HEADER_VALUE, request.getHeader("Authorization"));
            if (assertAssistantHeader) {
                assertEquals(ASSISTANT_HEADER_VALUE, request.getHeader("OpenAI-Beta"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception on executeRequestAssertions method call!", e);
        }
    }

    public void executeResponseAssertions(Object expectedResponse, Object actualResponse) {
        assertNotNull(expectedResponse);
        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
    }

    protected abstract String expectedURI();
    protected abstract String mockJsonResponse();
    protected abstract void shouldSendRequest();

}
