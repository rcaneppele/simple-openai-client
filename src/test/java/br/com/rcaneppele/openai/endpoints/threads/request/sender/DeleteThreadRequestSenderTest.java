package br.com.rcaneppele.openai.endpoints.threads.request.sender;

import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.common.response.DeletionStatus;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeleteThreadRequestSenderTest extends BaseRequestSenderTest {

    private static final String ASSISTANT_HEADER = "assistants=v1";
    private static final String THREAD_ID = "thread_123";

    private RequestSender<Void, DeletionStatus> sender;

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
                          "object": "thread.deleted",
                          "deleted": true
                        }
                        """);
    }

    @BeforeEach
    void setUp() {
        this.sender = new DeleteThreadRequestSender(this.url, TIMEOUT, API_KEY, THREAD_ID);
    }

    @Test
    public void shouldSendRequest() throws InterruptedException {
        var response = sender.sendRequest(null);
        var httpRequest = server.takeRequest();
        executeCommonAssertions(httpRequest, "", 1, "DELETE");

        assertEquals(ASSISTANT_HEADER, httpRequest.getHeader("OpenAI-Beta"));
        assertNotNull(response);
        assertEquals(THREAD_ID, response.id());
        assertEquals("thread.deleted", response.object());
        assertTrue(response.deleted());
    }

}