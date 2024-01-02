package br.com.rcaneppele.openai.endpoints.assistant.request.sender;

import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeleteAssistantFileRequestSenderTest extends BaseRequestSenderTest {

    private static final String ASSISTANT_HEADER = "assistants=v1";
    private static final String ASSISTANT_ID = "asst_123";
    private static final String FILE_ID = "file-123";

    private DeleteAssistantFileRequestSender sender;

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
                          "object": "assistant.file.deleted",
                          "deleted": true
                        }
                        """);
    }

    @BeforeEach
    void setUp() {
        this.sender = new DeleteAssistantFileRequestSender(this.url, TIMEOUT, API_KEY, ASSISTANT_ID, FILE_ID);
    }

    @Test
    public void shouldSendDeleteAssistantRequest() throws InterruptedException {
        var response = sender.sendRequest(null);
        var httpRequest = server.takeRequest();
        executeCommonAssertions(httpRequest, "", 1, "DELETE");

        assertEquals(ASSISTANT_HEADER, httpRequest.getHeader("OpenAI-Beta"));
        assertNotNull(response);
        assertEquals(FILE_ID, response.id());
        assertEquals("assistant.file.deleted", response.object());
        assertTrue(response.deleted());
    }

}
