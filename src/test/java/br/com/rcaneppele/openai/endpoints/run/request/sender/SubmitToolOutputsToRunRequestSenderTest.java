package br.com.rcaneppele.openai.endpoints.run.request.sender;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.assistant.tools.Function;
import br.com.rcaneppele.openai.endpoints.assistant.tools.Tool;
import br.com.rcaneppele.openai.endpoints.assistant.tools.ToolType;
import br.com.rcaneppele.openai.endpoints.run.request.SubmitToolOutputsToRunRequest;
import br.com.rcaneppele.openai.endpoints.run.request.builder.SubmitToolOutputsToRunRequestBuilder;
import br.com.rcaneppele.openai.endpoints.run.response.Run;
import br.com.rcaneppele.openai.endpoints.run.response.RunStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

class SubmitToolOutputsToRunRequestSenderTest extends BaseRequestSenderTest {

    private static final String THREAD_ID = "thread_123";
    private static final String RUN_ID = "run_123";

    private RequestSender<SubmitToolOutputsToRunRequest, Run> sender;
    private JsonConverter<SubmitToolOutputsToRunRequest> jsonConverter;
    private SubmitToolOutputsToRunRequestBuilder builder;

    @Override
    protected String expectedURI() {
        return "threads/" + THREAD_ID +"/runs/" +RUN_ID +"/submit_tool_outputs";
    }

    @Override
    protected String mockJsonResponse() {
        return """
                {
                  "id": "run_123",
                  "object": "thread.run",
                  "created_at": 1699075590,
                  "assistant_id": "asst_123",
                  "thread_id": "thread_123",
                  "status": "queued",
                  "started_at": 1699075591,
                  "expires_at": 1699076192,
                  "cancelled_at": null,
                  "failed_at": null,
                  "completed_at": null,
                  "last_error": null,
                  "model": "gpt-4",
                  "instructions": "You tell the weather.",
                  "tools": [
                    {
                      "type": "function",
                      "function": {
                        "name": "get_weather",
                        "description": "Determine weather in my location",
                        "parameters": {
                          "type": "object",
                          "properties": {
                            "location": {
                              "type": "string",
                              "description": "The city and state e.g. San Francisco, CA"
                            },
                            "unit": {
                              "type": "string",
                              "enum": [
                                "c",
                                "f"
                              ]
                            }
                          },
                          "required": [
                            "location"
                          ]
                        }
                      }
                    }
                  ],
                  "file_ids": [],
                  "metadata": {}
                }
                """;
    }

    @BeforeEach
    void setUp() {
        this.sender = new SubmitToolOutputsToRunRequestSender(this.url, TIMEOUT, API_KEY, THREAD_ID, RUN_ID);
        this.jsonConverter = new JsonConverter<>(SubmitToolOutputsToRunRequest.class);
        this.builder = new SubmitToolOutputsToRunRequestBuilder();
    }

    @Test
    public void shouldSendRequest() {
        var request = builder
                .threadId(THREAD_ID)
                .runId(RUN_ID)
                .toolOutput("call_123", "28C")
                .build();

        var actualResponse = sender.sendRequest(request);
        var expectedResponse = new Run(
                RUN_ID,
                "thread.run",
                Instant.ofEpochSecond(1699075590),
                "asst_123",
                THREAD_ID,
                RunStatus.QUEUED,
                null,
                null,
                Instant.ofEpochSecond(1699075591),
                Instant.ofEpochSecond(1699076192),
                null,
                null,
                null,
                OpenAIModel.GPT_4,
                "You tell the weather.",
                Set.of(new Tool(ToolType.FUNCTION_CALL.getName(), createFunction())),
                Set.of(),
                Map.of());
        var expectedRequestBody = jsonConverter.convertRequestToJson(request);
        executeRequestAssertions(expectedRequestBody, 1, HttpMethod.POST, true);
        executeResponseAssertions(expectedResponse, actualResponse);
    }

    private Function createFunction() {
        var properties = new LinkedHashMap<String, Object>();

        var location = new LinkedHashMap<String, Object>();
        location.put("type", "string");
        location.put("description", "The city and state e.g. San Francisco, CA");

        var unit = new LinkedHashMap<String, Object>();
        unit.put("type", "string");
        unit.put("enum", Arrays.asList("c", "f"));

        properties.put("location", location);
        properties.put("unit", unit);

        var required = Arrays.asList("location");

        var parameters = new LinkedHashMap<String, Object>();
        parameters.put("type", "object");
        parameters.put("properties", properties);
        parameters.put("required", required);

        return new Function(
                "get_weather",
                "Determine weather in my location",
                parameters
        );
    }

}
