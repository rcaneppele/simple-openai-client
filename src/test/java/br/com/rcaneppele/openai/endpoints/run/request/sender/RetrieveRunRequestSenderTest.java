package br.com.rcaneppele.openai.endpoints.run.request.sender;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.assistant.tools.Tool;
import br.com.rcaneppele.openai.endpoints.assistant.tools.ToolType;
import br.com.rcaneppele.openai.endpoints.run.response.Run;
import br.com.rcaneppele.openai.endpoints.run.response.RunStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

class RetrieveRunRequestSenderTest extends BaseRequestSenderTest {

    private static final String THREAD_ID = "thread_123";
    private static final String RUN_ID = "run_123";

    private RequestSender<Void, Run> sender;

    @Override
    protected String expectedURI() {
        return "threads/" + THREAD_ID +"/runs/" +RUN_ID;
    }

    @Override
    protected String mockJsonResponse() {
        return """
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
                """;
    }

    @BeforeEach
    void setUp() {
        this.sender = new RetrieveRunRequestSender(this.url, TIMEOUT, API_KEY, THREAD_ID, RUN_ID);
    }

    @Test
    public void shouldSendRequest() {
        var actualResponse = sender.sendRequest(null);
        var expectedResponse = new Run(
                RUN_ID,
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
                Map.of());

        executeRequestAssertions("", 1, HttpMethod.GET, true);
        executeResponseAssertions(expectedResponse, actualResponse);
    }

}
