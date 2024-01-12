package br.com.rcaneppele.openai.endpoints.assistant.request.sender;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.assistant.response.Assistant;
import br.com.rcaneppele.openai.endpoints.assistant.tools.Tool;
import br.com.rcaneppele.openai.endpoints.assistant.tools.ToolType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

class RetrieveAssistantRequestSenderTest extends BaseRequestSenderTest {

    private static final String ASSISTANT_ID = "asst_123";

    private RequestSender<Void, Assistant> sender;

    @Override
    protected String expectedURI() {
        return "assistants/" +ASSISTANT_ID;
    }

    @Override
    protected String mockJsonResponse() {
        return """
                {
                  "id": "asst_123",
                  "object": "assistant",
                  "created_at": 1699009709,
                  "name": "HR Helper",
                  "description": "Description",
                  "model": "gpt-4",
                  "instructions": "You are an HR bot.",
                  "tools": [
                    {
                      "type": "retrieval"
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
        this.sender = new RetrieveAssistantRequestSender(this.url, TIMEOUT, API_KEY, ASSISTANT_ID);
    }

    @Test
    public void shouldSendRequest() {
        var actualResponse = sender.sendRequest(null);
        var expectedResponse = new Assistant(
                ASSISTANT_ID,
                "assistant",
                Instant.ofEpochSecond(1699009709),
                "HR Helper",
                "Description",
                OpenAIModel.GPT_4,
                "You are an HR bot.",
                Set.of(new Tool(ToolType.RETRIEVAL.getName(), null)),
                Set.of("file-123"),
                Map.of()
        );

        executeRequestAssertions("", 1, HttpMethod.GET, true);
        executeResponseAssertions(expectedResponse, actualResponse);
    }

}
