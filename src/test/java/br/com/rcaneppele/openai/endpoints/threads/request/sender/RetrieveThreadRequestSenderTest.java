package br.com.rcaneppele.openai.endpoints.threads.request.sender;

import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class RetrieveThreadRequestSenderTest extends BaseRequestSenderTest {

    private static final String ASSISTANT_HEADER = "assistants=v1";
    private static final String THREAD_ID = "thread_123";

    private RetrieveThreadRequestSender sender;

    @Override
    protected String expectedURI() {
        return "threads/" + THREAD_ID;
    }

    @Override
    protected MockResponse mockResponse() {
        return new MockResponse()
                .setResponseCode(200)
                .setBody("""
                        {
                          "id": "thread_123",
                          "object": "thread",
                          "created_at": 1699014083,
                          "metadata": {}
                        }
                        """);
    }

    @BeforeEach
    void setUp() {
        this.sender = new RetrieveThreadRequestSender(this.url, TIMEOUT, API_KEY, THREAD_ID);
    }

    @Test
    public void shouldSendRetrieveThreadRequest() throws InterruptedException {
        var response = sender.sendRequest(null);
        var httpRequest = server.takeRequest();
        executeCommonAssertions(httpRequest, "", 1, "GET");

        assertEquals(ASSISTANT_HEADER, httpRequest.getHeader("OpenAI-Beta"));
        assertNotNull(response);
        assertEquals(THREAD_ID, response.id());
        assertEquals("thread", response.object());
        assertEquals(Instant.ofEpochSecond(1699014083), response.createdAt());
        assertTrue(response.metadata().isEmpty());
    }

}