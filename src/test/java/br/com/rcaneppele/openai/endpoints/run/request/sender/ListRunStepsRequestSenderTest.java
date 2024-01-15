package br.com.rcaneppele.openai.endpoints.run.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.QueryParameters;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.common.request.builder.QueryParametersBuilder;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.run.response.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

class ListRunStepsRequestSenderTest extends BaseRequestSenderTest {

    private static final String THREAD_ID = "thread_123";
    private static final String RUN_ID = "run_123";

    private RequestSender<QueryParameters, ListOfRunSteps> sender;
    private QueryParametersBuilder builder;

    @Override
    protected String expectedURI() {
        return "threads/" + THREAD_ID +"/runs/" +RUN_ID +"/steps?limit=1&order=asc&after=after_id&before=before_id";
    }

    @Override
    protected String mockJsonResponse() {
        return """
                {
                  "object": "list",
                  "data": [
                    {
                      "id": "step_123",
                      "object": "thread.run.step",
                      "created_at": 1699063291,
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
                  ],
                  "first_id": "run_123",
                  "last_id": "run_123",
                  "has_more": true
                }
                """;
    }

    @BeforeEach
    void setUp() {
        this.sender = new ListRunStepsRequestSender(this.url, TIMEOUT, API_KEY, THREAD_ID, RUN_ID);
        this.builder = new QueryParametersBuilder();
    }

    @Test
    public void shouldSendRequest() {
        var request = builder
                .limit(1)
                .ascOrder()
                .after("after_id")
                .before("before_id")
                .build();

        var actualResponse = sender.sendRequest(request);
        var expectedResponse = new ListOfRunSteps(
                "list",
                "run_123",
                "run_123",
                true,
                List.of(
                        new RunStep(
                                "step_123",
                                "thread.run.step",
                                Instant.ofEpochSecond(1699063291),
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
                                null)
                ));

        executeRequestAssertions("", 1, HttpMethod.GET, true);
        executeResponseAssertions(expectedResponse, actualResponse);
    }

}
