package br.com.rcaneppele.openai.endpoints.assistant.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.common.response.DeletionStatus;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeleteAssistantRequestSenderTest extends BaseRequestSenderTest {

    private static final String ASSISTANT_ID = "asst_123";

    private RequestSender<Void, DeletionStatus> sender;

    @Override
    protected String expectedURI() {
        return "assistants/" +ASSISTANT_ID;
    }

    @Override
    protected String mockJsonResponse() {
        return """
                {
                  "id": "asst_123",
                  "object": "assistant.deleted",
                  "deleted": true
                }
                """;
    }

    @BeforeEach
    void setUp() {
        this.sender = new DeleteAssistantRequestSender(this.url, TIMEOUT, API_KEY, ASSISTANT_ID);
    }

    @Test
    public void shouldSendRequest() {
        var actualResponse = sender.sendRequest(null);
        var expectedResponse = new DeletionStatus(ASSISTANT_ID, "assistant.deleted", true);

        executeRequestAssertions("", 1, HttpMethod.DELETE, true);
        executeResponseAssertions(expectedResponse, actualResponse);
    }

}
