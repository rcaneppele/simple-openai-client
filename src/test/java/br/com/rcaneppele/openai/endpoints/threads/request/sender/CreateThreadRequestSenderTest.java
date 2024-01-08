package br.com.rcaneppele.openai.endpoints.threads.request.sender;

import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.threads.request.CreateThreadRequest;
import br.com.rcaneppele.openai.endpoints.threads.request.builder.CreateThreadRequestBuilder;
import br.com.rcaneppele.openai.endpoints.threads.response.Thread;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CreateThreadRequestSenderTest extends BaseRequestSenderTest {

    private static final String ASSISTANT_HEADER = "assistants=v1";

    private RequestSender<CreateThreadRequest, Thread> sender;
    private JsonConverter<CreateThreadRequest> jsonConverter;

    @Override
    protected String expectedURI() {
        return "threads";
    }

    @Override
    protected MockResponse mockResponse() {
        return new MockResponse()
                .setResponseCode(200)
                .setBody("""
                        {
                          "id": "thread_123",
                          "object": "thread",
                          "created_at": 1699012949,
                          "metadata": {}
                        }
                        """);
    }

    @BeforeEach
    void setUp() {
        this.sender = new CreateThreadRequestSender(url, TIMEOUT, API_KEY);
        this.jsonConverter = new JsonConverter<>(CreateThreadRequest.class);
    }

    @Test
    public void shouldSendCreateThreadRequest() throws InterruptedException {
        var message = "My message";
        var metadata = Map.of("key", "value");
        var fileIds = Set.of("file-1", "file-2");

        var request = new CreateThreadRequestBuilder()
                .metadata(metadata)
                .addUserMessage(message, fileIds, metadata)
                .build();

        var response = sender.sendRequest(request);
        var httpRequest = server.takeRequest();
        var expectedRequestBody = jsonConverter.convertRequestToJson(request);
        executeCommonAssertions(httpRequest, expectedRequestBody, 1, "POST");

        assertEquals(ASSISTANT_HEADER, httpRequest.getHeader("OpenAI-Beta"));
        assertNotNull(response);
        assertEquals("thread_123", response.id());
        assertEquals("thread", response.object());
        assertEquals(Instant.ofEpochSecond(1699012949), response.createdAt());
        assertTrue(response.metadata().isEmpty());
    }

}