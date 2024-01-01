package br.com.rcaneppele.openai.endpoints.assistant.request.sender;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.assistant.request.CreateAssistantRequest;
import br.com.rcaneppele.openai.endpoints.assistant.request.builder.CreateAssistantRequestBuilder;
import br.com.rcaneppele.openai.endpoints.assistant.tools.Tool;
import br.com.rcaneppele.openai.endpoints.assistant.tools.ToolType;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class CreateAssistantRequestSenderTest extends BaseRequestSenderTest {

    private static final String ASSISTANT_HEADER = "assistants=v1";

    private CreateAssistantRequestSender sender;
    private JsonConverter<CreateAssistantRequest> jsonConverter;


    @Override
    protected String expectedURI() {
        return "assistants";
    }

    @Override
    protected MockResponse mockResponse() {
        return new MockResponse()
                .setResponseCode(200)
                .setBody("""
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
                        """);
    }

    @BeforeEach
    void setUp() {
        this.sender = new CreateAssistantRequestSender(url, TIMEOUT, API_KEY);
        this.jsonConverter = new JsonConverter<>(CreateAssistantRequest.class);
    }

    @Test
    public void shouldSendCreateAssistantRequest() throws InterruptedException {
        var name = "Math Tutor";
        var description = "Math Tutor description";
        var instructions = "You are a personal math tutor.";
        var model = OpenAIModel.GPT_4_1106_PREVIEW;
        var request = new CreateAssistantRequestBuilder()
                .model(model)
                .name(name)
                .description(description)
                .instructions(instructions)
                .codeInterpreter()
                .build();

        var response = sender.sendRequest(request);
        var httpRequest = server.takeRequest();
        var expectedRequestBody = jsonConverter.convertRequestToJson(request);
        executeCommonAssertions(httpRequest, expectedRequestBody, 1, "POST");

        assertEquals(ASSISTANT_HEADER, httpRequest.getHeader("OpenAI-Beta"));
        assertNotNull(response);
        assertEquals("asst_123", response.id());
        assertEquals("assistant", response.object());
        assertEquals(Instant.ofEpochSecond(1698984975), response.createdAt());
        assertEquals(name, response.name());
        assertEquals(description, response.description());
        assertEquals(model.getName(), response.model());
        assertEquals(instructions, response.instructions());
        assertEquals(1, response.tools().size());
        assertTrue(response.tools().contains(new Tool(
                ToolType.CODE_INTERPRETER.getName(), null
        )));
        assertTrue(response.metadata().isEmpty());
        assertTrue(response.fileIds().isEmpty());
    }

}