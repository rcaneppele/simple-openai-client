package br.com.rcaneppele.openai.endpoints.assistant.request.sender;

import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.assistant.response.DeletionStatus;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeleteAssistantRequestSenderTest extends BaseRequestSenderTest {

    private static final String ASSISTANT_HEADER = "assistants=v1";
    private static final String ASSISTANT_ID = "asst_123";

    private RequestSender<Void, DeletionStatus> sender;

    @Override
    protected String expectedURI() {
        return "assistants/" +ASSISTANT_ID;
    }

    @Override
    protected MockResponse mockResponse() {
        return new MockResponse()
                .setResponseCode(200)
                .setBody("""
                        {
                           "id": "asst_123",
                           "object": "assistant.deleted",
                           "deleted": true
                         }
                        """);
    }

    @BeforeEach
    void setUp() {
        this.sender = new DeleteAssistantRequestSender(this.url, TIMEOUT, API_KEY, ASSISTANT_ID);
    }

    @Test
    public void shouldSendRequest() throws InterruptedException {
        var response = sender.sendRequest(null);
        var httpRequest = server.takeRequest();
        executeCommonAssertions(httpRequest, "", 1, "DELETE");

        assertEquals(ASSISTANT_HEADER, httpRequest.getHeader("OpenAI-Beta"));
        assertNotNull(response);
        assertEquals(ASSISTANT_ID, response.id());
        assertEquals("assistant.deleted", response.object());
        assertTrue(response.deleted());
    }

}
