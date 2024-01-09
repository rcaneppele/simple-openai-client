package br.com.rcaneppele.openai.endpoints.thread.request.sender;

import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.thread.request.ModifyThreadRequest;
import br.com.rcaneppele.openai.endpoints.thread.request.builder.ModifyThreadRequestBuilder;
import br.com.rcaneppele.openai.endpoints.thread.response.Thread;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

class ModifyThreadRequestSenderTest extends BaseRequestSenderTest {

    private static final String THREAD_ID = "thread_123";

    private RequestSender<ModifyThreadRequest, Thread> sender;
    private JsonConverter<ModifyThreadRequest> jsonConverter;
    private ModifyThreadRequestBuilder builder;

    @Override
    protected String expectedURI() {
        return "threads/" + THREAD_ID;
    }

    @Override
    protected String mockJsonResponse() {
        return """
                {
                  "id": "thread_123",
                  "object": "thread",
                  "created_at": 1699014083,
                  "metadata": {
                    "modified": "true",
                    "user": "user-123"
                  }
                }
                """;
    }

    @BeforeEach
    void setUp() {
        this.sender = new ModifyThreadRequestSender(this.url, TIMEOUT, API_KEY, THREAD_ID);
        this.jsonConverter = new JsonConverter<>(ModifyThreadRequest.class);
        this.builder = new ModifyThreadRequestBuilder();
    }

    @Test
    public void shouldSendRequest() {
        var request = (ModifyThreadRequest) builder
                .threadId(THREAD_ID)
                .metadata(Map.of("modified", "true", "user", "user-123"))
                .build();

        var actualResponse = sender.sendRequest(request);
        var expectedResponse = new Thread(THREAD_ID, "thread", Instant.ofEpochSecond(1699014083), Map.of("modified", "true", "user", "user-123"));
        var expectedRequestBody = jsonConverter.convertRequestToJson(request);
        executeRequestAssertions(expectedRequestBody, 1, HttpMethod.POST, true);
        executeResponseAssertions(expectedResponse, actualResponse);
    }

}
