package br.com.rcaneppele.openai.endpoints.assistant.request.sender;

import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.assistant.request.ListAssistantsRequest;
import br.com.rcaneppele.openai.endpoints.assistant.request.builder.ListAssistantsRequestBuilder;
import br.com.rcaneppele.openai.endpoints.assistant.response.ListOfAssistants;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListAssistantsRequestSenderTest extends BaseRequestSenderTest {

    private static final String ASSISTANT_HEADER = "assistants=v1";

    private RequestSender<ListAssistantsRequest, ListOfAssistants> sender;

    @Override
    protected String expectedURI() {
        return "assistants?limit=2&order=asc&after=after_id&before=before_id";
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
                              "id": "asst_abc123",
                              "object": "assistant",
                              "created_at": 1698982736,
                              "name": "Coding Tutor",
                              "description": null,
                              "model": "gpt-4",
                              "instructions": "You are a helpful assistant designed to make me better at coding!",
                              "tools": [],
                              "file_ids": [],
                              "metadata": {}
                            },
                            {
                              "id": "asst_abc456",
                              "object": "assistant",
                              "created_at": 1698982718,
                              "name": "My Assistant",
                              "description": null,
                              "model": "gpt-4",
                              "instructions": "You are a helpful assistant designed to make me better at coding!",
                              "tools": [],
                              "file_ids": [],
                              "metadata": {}
                            }
                          ],
                          "first_id": "asst_abc123",
                          "last_id": "asst_abc456",
                          "has_more": true
                        }
                        """);
    }

    @BeforeEach
    void setUp() {
        this.sender = new ListAssistantsRequestSender(this.url, TIMEOUT, API_KEY);
    }

    @Test
    public void shouldSendListAssistantsRequest() throws InterruptedException {
        var request = (ListAssistantsRequest) new ListAssistantsRequestBuilder()
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
        assertEquals("asst_abc123", response.firstId());
        assertEquals("asst_abc456", response.lastId());
        assertTrue(response.hasMore());
        assertEquals(2, response.data().size());
        assertEquals("asst_abc123", response.data().get(0).id());
        assertEquals("Coding Tutor", response.data().get(0).name());
        assertEquals("asst_abc456", response.data().get(1).id());
        assertEquals("My Assistant", response.data().get(1).name());
    }

}