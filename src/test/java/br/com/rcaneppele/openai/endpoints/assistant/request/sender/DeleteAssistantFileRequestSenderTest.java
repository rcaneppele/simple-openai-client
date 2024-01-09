package br.com.rcaneppele.openai.endpoints.assistant.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.common.response.DeletionStatus;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeleteAssistantFileRequestSenderTest extends BaseRequestSenderTest {

    private static final String ASSISTANT_ID = "asst_123";
    private static final String FILE_ID = "file-123";

    private RequestSender<Void, DeletionStatus> sender;

    @Override
    protected String expectedURI() {
        return "assistants/" +ASSISTANT_ID + "/files/" +FILE_ID;
    }

    @Override
    protected String mockJsonResponse() {
        return """
                {
                  "id": "file-123",
                  "object": "assistant.file.deleted",
                  "deleted": true
                }
                """;
    }

    @BeforeEach
    void setUp() {
        this.sender = new DeleteAssistantFileRequestSender(this.url, TIMEOUT, API_KEY, ASSISTANT_ID, FILE_ID);
    }

    @Test
    public void shouldSendRequest() {
        var actualResponse = sender.sendRequest(null);
        var expectedResponse = new DeletionStatus(FILE_ID, "assistant.file.deleted", true);

        executeRequestAssertions("", 1, HttpMethod.DELETE, true);
        executeResponseAssertions(expectedResponse, actualResponse);
    }

}
