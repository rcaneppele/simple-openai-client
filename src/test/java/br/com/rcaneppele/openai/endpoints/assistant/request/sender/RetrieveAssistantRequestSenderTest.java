package br.com.rcaneppele.openai.endpoints.assistant.request.sender;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.assistant.response.Assistant;
import br.com.rcaneppele.openai.endpoints.assistant.tools.Tool;
import br.com.rcaneppele.openai.endpoints.assistant.tools.ToolType;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class RetrieveAssistantRequestSenderTest extends BaseRequestSenderTest {

    private static final String ASSISTANT_HEADER = "assistants=v1";
    private static final String ASSISTANT_ID = "asst_123";

    private RequestSender<Void, Assistant> sender;

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
                           "object": "assistant",
                           "created_at": 1699009709,
                           "name": "HR Helper",
                           "description": "Description",
                           "model": "gpt-4",
                           "instructions": "You are an HR bot.",
                           "tools": [
                             {
                               "type": "retrieval"
                             }
                           ],
                           "file_ids": [
                             "file-123"
                           ],
                           "metadata": {}
                         }
                        """);
    }

    @BeforeEach
    void setUp() {
        this.sender = new RetrieveAssistantRequestSender(this.url, TIMEOUT, API_KEY, ASSISTANT_ID);
    }

    @Test
    public void shouldSendRetrieveAssistantRequest() throws InterruptedException {
        var response = sender.sendRequest(null);
        var httpRequest = server.takeRequest();
        executeCommonAssertions(httpRequest, "", 1, "GET");

        assertEquals(ASSISTANT_HEADER, httpRequest.getHeader("OpenAI-Beta"));
        assertNotNull(response);
        assertEquals(ASSISTANT_ID, response.id());
        assertEquals("assistant", response.object());
        assertEquals(Instant.ofEpochSecond(1699009709), response.createdAt());
        assertEquals("HR Helper", response.name());
        assertEquals("Description", response.description());
        assertEquals(OpenAIModel.GPT_4.getName(), response.model());
        assertEquals("You are an HR bot.", response.instructions());
        assertEquals(1, response.tools().size());
        assertTrue(response.tools().contains(
                new Tool(ToolType.RETRIEVAL.getName(), null)
        ));
        assertTrue(response.fileIds().contains("file-123"));
        assertTrue(response.metadata().isEmpty());
    }

}
