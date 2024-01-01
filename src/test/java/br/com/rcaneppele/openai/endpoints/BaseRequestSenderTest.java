package br.com.rcaneppele.openai.endpoints;

import okhttp3.MediaType;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class BaseRequestSenderTest {

    protected static final Duration TIMEOUT = Duration.ofSeconds(2);
    protected static final String API_KEY = "fake-api-key";
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static final String AUTH_HEADER = "Bearer " +API_KEY;
    private static final String TEST_BASE_URL = "/test/";

    protected MockWebServer server;
    protected String url;

    @BeforeEach
    void baseSetUp() throws IOException {
        this.server = new MockWebServer();
        this.server.start();
        this.url = this.server.url(TEST_BASE_URL).url().toString();
        this.server.enqueue(mockResponse());
    }

    @AfterEach
    void baseTearDown() throws IOException {
        server.shutdown();
    }

    public void executeCommonAssertions(RecordedRequest request, String expectedRequestBody, int expectedRequestCount, String expectedHttpMethod) {
        assertEquals(expectedRequestCount, server.getRequestCount());
        assertEquals(expectedHttpMethod, request.getMethod());
        assertEquals(TEST_BASE_URL + expectedURI(), request.getPath());
        assertEquals(MEDIA_TYPE.toString(), request.getHeader("Content-Type"));
        assertEquals(AUTH_HEADER, request.getHeader("Authorization"));
        assertEquals(expectedRequestBody, request.getBody().readUtf8());
    }

    protected abstract String expectedURI();
    protected abstract MockResponse mockResponse();

}
