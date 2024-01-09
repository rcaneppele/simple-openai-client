package br.com.rcaneppele.openai.endpoints.assistant.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.assistant.response.AssistantFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class RetrieveAssistantFileRequestSenderTest extends BaseRequestSenderTest {

    private static final String ASSISTANT_ID = "asst_123";
    private static final String FILE_ID = "file-123";

    private RequestSender<Void, AssistantFile> sender;

    @Override
    protected String expectedURI() {
        return "assistants/" +ASSISTANT_ID + "/files/" +FILE_ID;
    }

    @Override
    protected String mockJsonResponse() {
        return """
                {
                  "id": "file-123",
                  "object": "assistant.file",
                  "created_at": 1699055364,
                  "assistant_id": "asst_123"
                }
                """;
    }

    @BeforeEach
    void setUp() {
        this.sender = new RetrieveAssistantFileRequestSender(this.url, TIMEOUT, API_KEY, ASSISTANT_ID, FILE_ID);
    }

    @Test
    public void shouldSendRequest() {
        var actualResponse = sender.sendRequest(null);
        var expectedResponse = new AssistantFile(FILE_ID, "assistant.file", Instant.ofEpochSecond(1699055364), ASSISTANT_ID);

        executeRequestAssertions("", 1, HttpMethod.GET, true);
        executeResponseAssertions(expectedResponse, actualResponse);
    }

}
