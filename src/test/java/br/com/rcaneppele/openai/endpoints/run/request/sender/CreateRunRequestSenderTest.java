package br.com.rcaneppele.openai.endpoints.run.request.sender;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.assistant.tools.Tool;
import br.com.rcaneppele.openai.endpoints.assistant.tools.ToolType;
import br.com.rcaneppele.openai.endpoints.run.request.CreateRunRequest;
import br.com.rcaneppele.openai.endpoints.run.request.builder.CreateRunRequestBuilder;
import br.com.rcaneppele.openai.endpoints.run.response.Run;
import br.com.rcaneppele.openai.endpoints.run.response.RunStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

class CreateRunRequestSenderTest extends BaseRequestSenderTest {

    private static final String THREAD_ID = "thread_123";
    private static final String ASSISTANT_ID = "asst_123";

    private RequestSender<CreateRunRequest, Run> sender;
    private JsonConverter<CreateRunRequest> jsonConverter;
    private CreateRunRequestBuilder builder;

    @Override
    protected String expectedURI() {
        return "threads/" + THREAD_ID +"/runs";
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
                  "required_action": null,
                  "last_error": null,
                  "started_at": 1699063291,
                  "expires_at": null,
                  "cancelled_at": null,
                  "failed_at": null,
                  "completed_at": 1699063292,
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
                  "metadata": {}
                }
                """;
    }

    @BeforeEach
    void setUp() {
        this.sender = new CreateRunRequestSender(this.url, TIMEOUT, API_KEY, THREAD_ID);
        this.jsonConverter = new JsonConverter<>(CreateRunRequest.class);
        this.builder = new CreateRunRequestBuilder();
    }

    @Test
    public void shouldSendRequest() {
        var request = builder
                .assistantId(ASSISTANT_ID)
                .build();

        var actualResponse = sender.sendRequest(request);
        var expectedResponse = new Run(
                "run_123",
                "thread.run",
                Instant.ofEpochSecond(1699063290),
                ASSISTANT_ID,
                THREAD_ID,
                RunStatus.COMPLETED,
                null,
                null,
                Instant.ofEpochSecond(1699063291),
                null,
                null,
                null,
                Instant.ofEpochSecond(1699063292),
                OpenAIModel.GPT_4,
                null,
                Set.of(
                        new Tool(ToolType.CODE_INTERPRETER.getName(), null)
                ),
                Set.of("file-123"),
                Map.of());
        var expectedRequestBody = jsonConverter.convertRequestToJson(request);
        executeRequestAssertions(expectedRequestBody, 1, HttpMethod.POST, true);
        executeResponseAssertions(expectedResponse, actualResponse);
    }

}
