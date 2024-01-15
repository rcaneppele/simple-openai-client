package br.com.rcaneppele.openai.endpoints.run.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.run.response.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class RetrieveRunStepRequestSenderTest extends BaseRequestSenderTest {

    private static final String THREAD_ID = "thread_123";
    private static final String RUN_ID = "run_123";
    private static final String STEP_ID = "step_123";

    private RequestSender<Void, RunStep> sender;

    @Override
    protected String expectedURI() {
        return "threads/" + THREAD_ID +"/runs/" +RUN_ID +"/steps/" +STEP_ID;
    }

    @Override
    protected String mockJsonResponse() {
        return """
                {
                  "id": "step_123",
                  "object": "thread.run.step",
                  "created_at": 1699063290,
                  "run_id": "run_123",
                  "assistant_id": "asst_123",
                  "thread_id": "thread_123",
                  "type": "message_creation",
                  "status": "completed",
                  "cancelled_at": null,
                  "completed_at": 1699063291,
                  "expired_at": null,
                  "failed_at": null,
                  "last_error": null,
                  "step_details": {
                    "type": "message_creation",
                    "message_creation": {
                      "message_id": "msg_123"
                    }
                  }
                }
                """;
    }

    @BeforeEach
    void setUp() {
        this.sender = new RetrieveRunStepRequestSender(this.url, TIMEOUT, API_KEY, THREAD_ID, RUN_ID, STEP_ID);
    }

    @Test
    public void shouldSendRequest() {
        var actualResponse = sender.sendRequest(null);
        var expectedResponse = new RunStep(
                STEP_ID,
                "thread.run.step",
                Instant.ofEpochSecond(1699063290),
                "asst_123",
                THREAD_ID,
                RUN_ID,
                RunStepType.MESSAGE_CREATION,
                RunStepStatus.COMPLETED,
                new RunStepDetails(
                        RunStepType.MESSAGE_CREATION.toValue(),
                        new MessageCreation("msg_123"),
                        null
                ),
                null,
                null,
                null,
                null,
                Instant.ofEpochSecond(1699063291),
                null);

        executeRequestAssertions("", 1, HttpMethod.GET, true);
        executeResponseAssertions(expectedResponse, actualResponse);
    }

}
