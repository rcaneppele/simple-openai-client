package br.com.rcaneppele.openai.endpoints.threads.request.sender;

import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.threads.request.ModifyThreadRequest;
import br.com.rcaneppele.openai.endpoints.threads.request.builder.ModifyThreadRequestBuilder;
import br.com.rcaneppele.openai.endpoints.threads.response.Thread;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ModifyThreadRequestSenderTest extends BaseRequestSenderTest {

    private static final String ASSISTANT_HEADER = "assistants=v1";
    private static final String THREAD_ID = "thread_123";

    private RequestSender<ModifyThreadRequest, Thread> sender;
    private JsonConverter<ModifyThreadRequest> jsonConverter;
    private ModifyThreadRequestBuilder builder;

    @Override
    protected String expectedURI() {
        return "threads/" + THREAD_ID;
    }

    @Override
    protected MockResponse mockResponse() {
        return new MockResponse()
                .setResponseCode(200)
                .setBody("""
                        {
                          "id": "thread_123",
                          "object": "thread",
                          "created_at": 1699014083,
                          "metadata": {
                            "modified": "true",
                            "user": "user-123"
                          }
                        }
                        """);
    }

    @BeforeEach
    void setUp() {
        this.sender = new ModifyThreadRequestSender(this.url, TIMEOUT, API_KEY, THREAD_ID);
        this.jsonConverter = new JsonConverter<>(ModifyThreadRequest.class);
        this.builder = new ModifyThreadRequestBuilder();
    }

    @Test
    public void shouldSendRequest() throws InterruptedException {
        var request = (ModifyThreadRequest) builder
                .threadId(THREAD_ID)
                .metadata(Map.of("modified", "true", "user", "user-123"))
                .build();

        var response = sender.sendRequest(request);
        var httpRequest = server.takeRequest();
        var expectedRequestBody = jsonConverter.convertRequestToJson(request);
        executeCommonAssertions(httpRequest, expectedRequestBody, 1, "POST");

        assertEquals(ASSISTANT_HEADER, httpRequest.getHeader("OpenAI-Beta"));
        assertNotNull(response);
        assertEquals(THREAD_ID, response.id());
        assertEquals("thread", response.object());
        assertEquals(Instant.ofEpochSecond(1699014083), response.createdAt());
        assertEquals(2, response.metadata().size());
        assertEquals("true", response.metadata().get("modified"));
        assertEquals("user-123", response.metadata().get("user"));
    }

}