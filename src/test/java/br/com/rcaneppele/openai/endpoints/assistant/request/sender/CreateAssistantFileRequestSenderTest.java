package br.com.rcaneppele.openai.endpoints.assistant.request.sender;

import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.assistant.request.CreateAssistantFileRequest;
import br.com.rcaneppele.openai.endpoints.assistant.request.builder.CreateAssistantFileRequestBuilder;
import br.com.rcaneppele.openai.endpoints.assistant.response.AssistantFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class CreateAssistantFileRequestSenderTest extends BaseRequestSenderTest {

    private static final String ASSISTANT_ID = "asst_123";

    private RequestSender<CreateAssistantFileRequest, AssistantFile> sender;
    private JsonConverter<CreateAssistantFileRequest> jsonConverter;
    private CreateAssistantFileRequestBuilder builder;

    @Override
    protected String expectedURI() {
        return "assistants/" + ASSISTANT_ID +"/files";
    }

    @Override
    protected String mockJsonResponse() {
        return """
                {
                  "id": "file-123",
                  "object": "assistant.file",
                  "created_at": 1699055364,
                  "assistant_id": "%s"
                }
                """.formatted(ASSISTANT_ID);
    }

    @BeforeEach
    void setUp() {
        this.sender = new CreateAssistantFileRequestSender(this.url, TIMEOUT, API_KEY, ASSISTANT_ID);
        this.jsonConverter = new JsonConverter<>(CreateAssistantFileRequest.class);
        this.builder = new CreateAssistantFileRequestBuilder();
    }

    @Test
    public void shouldSendRequest() {
        var request = builder
                .assistantId(ASSISTANT_ID)
                .fileId("file-123")
                .build();

        var actualResponse = sender.sendRequest(request);
        var expectedResponse = new AssistantFile("file-123", "assistant.file", Instant.ofEpochSecond(1699055364), ASSISTANT_ID);
        var expectedRequestBody = jsonConverter.convertRequestToJson(request);

        executeRequestAssertions(expectedRequestBody, 1, HttpMethod.POST, true);
        executeResponseAssertions(expectedResponse, actualResponse);
    }

}
