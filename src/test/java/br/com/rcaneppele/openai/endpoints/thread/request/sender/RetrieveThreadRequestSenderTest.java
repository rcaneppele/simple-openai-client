package br.com.rcaneppele.openai.endpoints.thread.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.thread.response.Thread;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

class RetrieveThreadRequestSenderTest extends BaseRequestSenderTest {

    private static final String THREAD_ID = "thread_123";

    private RequestSender<Void, Thread> sender;

    @Override
    protected String expectedURI() {
        return "threads/" + THREAD_ID;
    }

    @Override
    protected String mockJsonResponse() {
        return """
                {
                  "id": "thread_123",
                  "object": "thread",
                  "created_at": 1699014083,
                  "metadata": {}
                }
                """;
    }

    @BeforeEach
    void setUp() {
        this.sender = new RetrieveThreadRequestSender(this.url, TIMEOUT, API_KEY, THREAD_ID);
    }

    @Test
    public void shouldSendRequest() {
        var actualResponse = sender.sendRequest(null);
        var expectedResponse = new Thread(THREAD_ID, "thread", Instant.ofEpochSecond(1699014083), Map.of());

        executeRequestAssertions("", 1, HttpMethod.GET, true);
        executeResponseAssertions(expectedResponse, actualResponse);
    }

}
