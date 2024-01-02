package br.com.rcaneppele.openai.endpoints.assistant.request.sender;

import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.assistant.request.CreateAssistantFileRequest;
import br.com.rcaneppele.openai.endpoints.assistant.request.builder.CreateAssistantFileRequestBuilder;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CreateAssistantFileRequestSenderTest extends BaseRequestSenderTest {

    private static final String ASSISTANT_HEADER = "assistants=v1";
    private static final String ASSISTANT_ID = "asst_123";

    private CreateAssistantFileRequestSender sender;
    private JsonConverter<CreateAssistantFileRequest> jsonConverter;

    @Override
    protected String expectedURI() {
        return "assistants/" + ASSISTANT_ID +"/files";
    }

    @Override
    protected MockResponse mockResponse() {
        return new MockResponse()
                .setResponseCode(200)
                .setBody("""
                        {
                          "id": "file-123",
                          "object": "assistant.file",
                          "created_at": 1699055364,
                          "assistant_id": "%s"
                        }
                        """.formatted(ASSISTANT_ID));
    }

    @BeforeEach
    void setUp() {
        this.sender = new CreateAssistantFileRequestSender(this.url, TIMEOUT, API_KEY, ASSISTANT_ID);
        this.jsonConverter = new JsonConverter<>(CreateAssistantFileRequest.class);
    }

    @Test
    public void shouldSendCreateAssistantFileRequest() throws InterruptedException {
        var fileId = "file-123";
        var request = new CreateAssistantFileRequestBuilder()
                .assistantId(ASSISTANT_ID)
                .fileId(fileId)
                .build();

        var response = sender.sendRequest(request);
        var httpRequest = server.takeRequest();
        var expectedRequestBody = jsonConverter.convertRequestToJson(request);
        executeCommonAssertions(httpRequest, expectedRequestBody, 1, "POST");

        assertEquals(ASSISTANT_HEADER, httpRequest.getHeader("OpenAI-Beta"));
        assertNotNull(response);
        assertEquals(fileId, response.id());
        assertEquals("assistant.file", response.object());
        assertEquals(Instant.ofEpochSecond(1699055364), response.createdAt());
        assertEquals(ASSISTANT_ID, response.assistantId());
    }

}