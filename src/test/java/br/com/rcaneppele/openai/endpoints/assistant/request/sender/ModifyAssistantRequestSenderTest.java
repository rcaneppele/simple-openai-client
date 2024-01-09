package br.com.rcaneppele.openai.endpoints.assistant.request.sender;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.assistant.request.ModifyAssistantRequest;
import br.com.rcaneppele.openai.endpoints.assistant.request.builder.ModifyAssistantRequestBuilder;
import br.com.rcaneppele.openai.endpoints.assistant.response.Assistant;
import br.com.rcaneppele.openai.endpoints.assistant.tools.Tool;
import br.com.rcaneppele.openai.endpoints.assistant.tools.ToolType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

class ModifyAssistantRequestSenderTest extends BaseRequestSenderTest {

    private static final String ASSISTANT_ID = "asst_123";

    private RequestSender<ModifyAssistantRequest, Assistant> sender;
    private JsonConverter<ModifyAssistantRequest> jsonConverter;
    private ModifyAssistantRequestBuilder builder;

    @Override
    protected String expectedURI() {
        return "assistants/" + ASSISTANT_ID;
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
        this.sender = new ModifyAssistantRequestSender(url, TIMEOUT, API_KEY, ASSISTANT_ID);
        this.jsonConverter = new JsonConverter<>(ModifyAssistantRequest.class);
        this.builder = new ModifyAssistantRequestBuilder();
    }

    @Test
    public void shouldSendRequest() {
        var name = "Math Tutor";
        var description = "Math Tutor description";
        var instructions = "You are a personal math tutor.";
        var model = OpenAIModel.GPT_4_1106_PREVIEW;
        var request = (ModifyAssistantRequest) builder
                .assistantId(ASSISTANT_ID)
                .model(model)
                .name(name)
                .description(description)
                .instructions(instructions)
                .codeInterpreter()
                .build();

        var actualResponse = sender.sendRequest(request);
        var expectedResponse = new Assistant(
                ASSISTANT_ID,
                "assistant",
                Instant.ofEpochSecond(1698984975),
                "Math Tutor",
                "Math Tutor description",
                OpenAIModel.GPT_4_1106_PREVIEW.getName(),
                "You are a personal math tutor.",
                Set.of(new Tool(ToolType.CODE_INTERPRETER.getName(), null)),
                Set.of(),
                Map.of()
        );
        var expectedRequestBody = jsonConverter.convertRequestToJson(request);

        executeRequestAssertions(expectedRequestBody, 1, HttpMethod.POST, true);
        executeResponseAssertions(expectedResponse, actualResponse);
    }

}
