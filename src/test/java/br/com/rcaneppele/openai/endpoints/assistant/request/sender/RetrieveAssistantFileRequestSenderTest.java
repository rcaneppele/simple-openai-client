package br.com.rcaneppele.openai.endpoints.assistant.request.sender;

import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.assistant.response.AssistantFile;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RetrieveAssistantFileRequestSenderTest extends BaseRequestSenderTest {

    private static final String ASSISTANT_HEADER = "assistants=v1";
    private static final String ASSISTANT_ID = "asst_123";
    private static final String FILE_ID = "file-123";

    private RequestSender<Void, AssistantFile> sender;

    @Override
    protected String expectedURI() {
        return "assistants/" +ASSISTANT_ID + "/files/" +FILE_ID;
    }

    @Override
    protected MockResponse mockResponse() {
        return new MockResponse()
                .setResponseCode(200)
                .setBody("""
                        {
                          "id": "file-123",
                          "object": "assistant.file",
                          "created_at": 1699055364,
                          "assistant_id": "asst_123"
                        }
                        """);
    }

    @BeforeEach
    void setUp() {
        this.sender = new RetrieveAssistantFileRequestSender(this.url, TIMEOUT, API_KEY, ASSISTANT_ID, FILE_ID);
    }

    @Test
    public void shouldSendRequest() throws InterruptedException {
        var response = sender.sendRequest(null);
        var httpRequest = server.takeRequest();
        executeCommonAssertions(httpRequest, "", 1, "GET");

        assertEquals(ASSISTANT_HEADER, httpRequest.getHeader("OpenAI-Beta"));
        assertNotNull(response);
        assertEquals(FILE_ID, response.id());
        assertEquals("assistant.file", response.object());
        assertEquals(Instant.ofEpochSecond(1699055364), response.createdAt());
        assertEquals(ASSISTANT_ID, response.assistantId());
    }

}
