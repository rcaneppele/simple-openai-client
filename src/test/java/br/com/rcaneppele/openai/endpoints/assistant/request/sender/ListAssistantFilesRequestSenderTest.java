package br.com.rcaneppele.openai.endpoints.assistant.request.sender;

import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.assistant.request.ListAssistantFilesRequest;
import br.com.rcaneppele.openai.endpoints.assistant.request.builder.ListAssistantFilesRequestBuilder;
import br.com.rcaneppele.openai.endpoints.assistant.response.ListOfAssistantFiles;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListAssistantFilesRequestSenderTest extends BaseRequestSenderTest {

    private static final String ASSISTANT_HEADER = "assistants=v1";
    private static final String ASSISTANT_ID = "asst_123";

    private RequestSender<ListAssistantFilesRequest, ListOfAssistantFiles> sender;

    @Override
    protected String expectedURI() {
        return "assistants/" +ASSISTANT_ID +"/files?limit=2&order=asc&after=after_id&before=before_id";
    }

    @Override
    protected MockResponse mockResponse() {
        return new MockResponse()
                .setResponseCode(200)
                .setBody("""
                        {
                          "object": "list",
                          "data": [
                            {
                               "id": "file-123",
                               "object": "assistant.file",
                               "created_at": 1699060412,
                               "assistant_id": "asst_123"
                             },
                             {
                               "id": "file-456",
                               "object": "assistant.file",
                               "created_at": 1699060412,
                               "assistant_id": "asst_123"
                             }
                          ],
                          "first_id": "file-123",
                          "last_id": "file-456",
                          "has_more": true
                        }
                        """);
    }

    @BeforeEach
    void setUp() {
        this.sender = new ListAssistantFilesRequestSender(this.url, TIMEOUT, API_KEY, ASSISTANT_ID);
    }

    @Test
    public void shouldSendListAssistantFilesRequest() throws InterruptedException {
        var request = (ListAssistantFilesRequest) new ListAssistantFilesRequestBuilder()
                .assistantId(ASSISTANT_ID)
                .limit(2)
                .ascOrder()
                .after("after_id")
                .before("before_id")
                .build();

        var response = sender.sendRequest(request);
        var httpRequest = server.takeRequest();
        executeCommonAssertions(httpRequest, "", 1, "GET");

        assertEquals(ASSISTANT_HEADER, httpRequest.getHeader("OpenAI-Beta"));
        assertNotNull(response);
        assertEquals("list", response.object());
        assertEquals("file-123", response.firstId());
        assertEquals("file-456", response.lastId());
        assertTrue(response.hasMore());
        assertEquals(2, response.data().size());
        assertEquals("file-123", response.data().get(0).id());
        assertEquals("assistant.file", response.data().get(0).object());
        assertEquals(ASSISTANT_ID, response.data().get(0).assistantId());
        assertEquals("file-456", response.data().get(1).id());
        assertEquals("assistant.file", response.data().get(1).object());
        assertEquals(ASSISTANT_ID, response.data().get(1).assistantId());
    }

}