package br.com.rcaneppele.openai.endpoints.run.request.sender;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.QueryParameters;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.common.request.builder.QueryParametersBuilder;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.assistant.tools.Tool;
import br.com.rcaneppele.openai.endpoints.assistant.tools.ToolType;
import br.com.rcaneppele.openai.endpoints.run.response.ListOfRuns;
import br.com.rcaneppele.openai.endpoints.run.response.Run;
import br.com.rcaneppele.openai.endpoints.run.response.RunStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

class ListRunsRequestSenderTest extends BaseRequestSenderTest {

    private static final String THREAD_ID = "thread_123";

    private RequestSender<QueryParameters, ListOfRuns> sender;
    private QueryParametersBuilder builder;

    @Override
    protected String expectedURI() {
        return "threads/" + THREAD_ID +"/runs?limit=1&order=asc&after=after_id&before=before_id";
    }

    @Override
    protected String mockJsonResponse() {
        return """
                {
                  "object": "list",
                  "data": [
                    {
                      "id": "run_123",
                      "object": "thread.run",
                      "created_at": 1699075072,
                      "assistant_id": "asst_123",
                      "thread_id": "thread_123",
                      "status": "completed",
                      "started_at": 1699075073,
                      "expires_at": null,
                      "cancelled_at": null,
                      "failed_at": null,
                      "completed_at": 1699075074,
                      "last_error": null,
                      "model": "gpt-3.5-turbo",
                      "instructions": null,
                      "tools": [
                        {
                          "type": "code_interpreter"
                        }
                      ],
                      "file_ids": [
                        "file-123"
                      ],
                      "metadata": {}
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
        this.sender = new ListRunsRequestSender(this.url, TIMEOUT, API_KEY, THREAD_ID);
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
        var expectedResponse = new ListOfRuns(
                "list",
                "run_123",
                "run_123",
                true,
                List.of(
                        new Run(
                                "run_123",
                                "thread.run",
                                Instant.ofEpochSecond(1699075072),
                                "asst_123",
                                THREAD_ID,
                                RunStatus.COMPLETED,
                                Instant.ofEpochSecond(1699075073),
                                null,
                                null,
                                null,
                                Instant.ofEpochSecond(1699075074),
                                null,
                                OpenAIModel.GPT_3_5_TURBO,
                                null,
                                Set.of(
                                        new Tool(ToolType.CODE_INTERPRETER.getName(), null)
                                ),
                                Set.of("file-123"),
                                Map.of())
                ));

        executeRequestAssertions("", 1, HttpMethod.GET, true);
        executeResponseAssertions(expectedResponse, actualResponse);
    }

}
