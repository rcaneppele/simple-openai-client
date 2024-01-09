package br.com.rcaneppele.openai.endpoints.threads.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.common.response.DeletionStatus;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeleteThreadRequestSenderTest extends BaseRequestSenderTest {

    private static final String THREAD_ID = "thread_123";

    private RequestSender<Void, DeletionStatus> sender;

    @Override
    protected String expectedURI() {
        return "threads/" + THREAD_ID;
    }

    @Override
    protected String mockJsonResponse() {
        return """
                {
                  "id": "thread_123",
                  "object": "thread.deleted",
                  "deleted": true
                }
                """;
    }

    @BeforeEach
    void setUp() {
        this.sender = new DeleteThreadRequestSender(this.url, TIMEOUT, API_KEY, THREAD_ID);
    }

    @Test
    public void shouldSendRequest() {
        var actualResponse = sender.sendRequest(null);
        var expectedResponse = new DeletionStatus(THREAD_ID, "thread.deleted", true);
        executeRequestAssertions("", 1, HttpMethod.DELETE, true);
        executeResponseAssertions(expectedResponse, actualResponse);
    }

}
