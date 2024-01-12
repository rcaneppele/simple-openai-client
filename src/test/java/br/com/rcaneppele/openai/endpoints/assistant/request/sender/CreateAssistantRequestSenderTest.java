package br.com.rcaneppele.openai.endpoints.assistant.request.sender;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.assistant.request.CreateAssistantRequest;
import br.com.rcaneppele.openai.endpoints.assistant.request.builder.CreateAssistantRequestBuilder;
import br.com.rcaneppele.openai.endpoints.assistant.response.Assistant;
import br.com.rcaneppele.openai.endpoints.assistant.tools.Tool;
import br.com.rcaneppele.openai.endpoints.assistant.tools.ToolType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

class CreateAssistantRequestSenderTest extends BaseRequestSenderTest {

    private RequestSender<CreateAssistantRequest, Assistant> sender;
    private JsonConverter<CreateAssistantRequest> jsonConverter;
    private CreateAssistantRequestBuilder builder;

    @Override
    protected String expectedURI() {
        return "assistants";
    }

    @Override
    protected String mockJsonResponse() {
        return """
                {
                  "id": "asst_123",
                  "object": "assistant",
                  "created_at": 1698984975,
                  "name": "Math Tutor",
                  "description": "Math Tutor description",
                  "model": "gpt-4-1106-preview",
                  "instructions": "You are a personal math tutor.",
                  "tools": [
                    {
                      "type": "code_interpreter"
                    }
                  ],
                  "file_ids": [],
                  "metadata": {}
                }
                """;
    }

    @BeforeEach
    void setUp() {
        this.sender = new CreateAssistantRequestSender(url, TIMEOUT, API_KEY);
        this.jsonConverter = new JsonConverter<>(CreateAssistantRequest.class);
        this.builder = new CreateAssistantRequestBuilder();
    }

    @Test
    public void shouldSendRequest() {
        var request = (CreateAssistantRequest) builder
                .model(OpenAIModel.GPT_4_1106_PREVIEW)
                .name("Math Tutor")
                .description("Math Tutor description")
                .instructions("You are a personal math tutor.")
                .codeInterpreter()
                .build();

        var actualResponse = sender.sendRequest(request);
        var expectedResponse = new Assistant(
                "asst_123",
                "assistant",
                Instant.ofEpochSecond(1698984975),
                "Math Tutor",
                "Math Tutor description",
                OpenAIModel.GPT_4_1106_PREVIEW,
                "You are a personal math tutor.",
                Set.of(new Tool(ToolType.CODE_INTERPRETER.getName(), null)),
                Set.of(),
                Map.of());
        var expectedRequestBody = jsonConverter.convertRequestToJson(request);

        executeRequestAssertions(expectedRequestBody, 1, HttpMethod.POST, true);
        executeResponseAssertions(expectedResponse, actualResponse);
    }

}