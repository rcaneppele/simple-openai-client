package br.com.rcaneppele.openai.endpoints.message.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.message.response.MessageFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class RetrieveMessageFileRequestSenderTest extends BaseRequestSenderTest {

    private static final String THREAD_ID = "thread_123";
    private static final String MESSAGE_ID = "msg_123";
    private static final String FILE_ID = "file-123";

    private RequestSender<Void, MessageFile> sender;

    @Override
    protected String expectedURI() {
        return "threads/" + THREAD_ID +"/messages/" +MESSAGE_ID +"/files/" +FILE_ID;
    }

    @Override
    protected String mockJsonResponse() {
        return """
                {
                  "id": "file-123",
                  "object": "thread.message.file",
                  "created_at": 1699061776,
                  "message_id": "msg_123"
                }
                """;
    }

    @BeforeEach
    void setUp() {
        this.sender = new RetrieveMessageFileRequestSender(this.url, TIMEOUT, API_KEY, THREAD_ID, MESSAGE_ID, FILE_ID);
    }

    @Test
    protected void shouldSendRequest() {
        var actualResponse = sender.sendRequest(null);
        var expectedResponse = new MessageFile(
                "file-123",
                "thread.message.file",
                Instant.ofEpochSecond(1699061776),
                "msg_123");

        executeRequestAssertions("", 1, HttpMethod.GET, true);
        executeResponseAssertions(expectedResponse, actualResponse);
    }

}
