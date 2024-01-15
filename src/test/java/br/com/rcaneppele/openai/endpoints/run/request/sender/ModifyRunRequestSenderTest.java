package br.com.rcaneppele.openai.endpoints.run.request.sender;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.assistant.tools.Tool;
import br.com.rcaneppele.openai.endpoints.assistant.tools.ToolType;
import br.com.rcaneppele.openai.endpoints.run.request.ModifyRunRequest;
import br.com.rcaneppele.openai.endpoints.run.request.builder.ModifyRunRequestBuilder;
import br.com.rcaneppele.openai.endpoints.run.response.Run;
import br.com.rcaneppele.openai.endpoints.run.response.RunStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

class ModifyRunRequestSenderTest extends BaseRequestSenderTest {

    private static final String THREAD_ID = "thread_123";
    private static final String RUN_ID = "run_123";

    private RequestSender<ModifyRunRequest, Run> sender;
    private JsonConverter<ModifyRunRequest> jsonConverter;
    private ModifyRunRequestBuilder builder;

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
                  "created_at": 1699063290,
                  "assistant_id": "asst_123",
                  "thread_id": "thread_123",
                  "status": "completed",
                  "started_at": 1699063291,
                  "expires_at": null,
                  "cancelled_at": null,
                  "failed_at": null,
                  "completed_at": 1699063292,
                  "last_error": null,
                  "model": "gpt-4",
                  "instructions": null,
                  "tools": [
                    {
                      "type": "code_interpreter"
                    }
                  ],
                  "file_ids": [
                    "file-123"
                  ],
                  "metadata": {
                    "user_id": "user_123"
                  }
                }
                """;
    }

    @BeforeEach
    void setUp() {
        this.sender = new ModifyRunRequestSender(this.url, TIMEOUT, API_KEY, THREAD_ID, RUN_ID);
        this.jsonConverter = new JsonConverter<>(ModifyRunRequest.class);
        this.builder = new ModifyRunRequestBuilder();
    }

    @Test
    public void shouldSendRequest() {
        var request = builder
                .threadId(THREAD_ID)
                .runId(RUN_ID)
                .metadata(Map.of("user_id", "user_123"))
                .build();

        var actualResponse = sender.sendRequest(request);
        var expectedResponse = new Run(
                RUN_ID,
                "thread.run",
                Instant.ofEpochSecond(1699063290),
                "asst_123",
                THREAD_ID,
                RunStatus.COMPLETED,
                Instant.ofEpochSecond(1699063291),
                null,
                null,
                null,
                Instant.ofEpochSecond(1699063292),
                null,
                OpenAIModel.GPT_4,
                null,
                Set.of(
                        new Tool(ToolType.CODE_INTERPRETER.getName(), null)
                ),
                Set.of("file-123"),
                Map.of("user_id", "user_123"));
        var expectedRequestBody = jsonConverter.convertRequestToJson(request);
        executeRequestAssertions(expectedRequestBody, 1, HttpMethod.POST, true);
        executeResponseAssertions(expectedResponse, actualResponse);
    }

}
