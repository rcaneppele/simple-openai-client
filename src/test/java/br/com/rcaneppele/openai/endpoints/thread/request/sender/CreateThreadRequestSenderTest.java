package br.com.rcaneppele.openai.endpoints.thread.request.sender;

import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.thread.request.CreateThreadRequest;
import br.com.rcaneppele.openai.endpoints.thread.request.builder.CreateThreadRequestBuilder;
import br.com.rcaneppele.openai.endpoints.thread.response.Thread;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

class CreateThreadRequestSenderTest extends BaseRequestSenderTest {

    private RequestSender<CreateThreadRequest, Thread> sender;
    private JsonConverter<CreateThreadRequest> jsonConverter;
    private CreateThreadRequestBuilder builder;

    @Override
    protected String expectedURI() {
        return "threads";
    }

    @Override
    protected String mockJsonResponse() {
        return """
                {
                  "id": "thread_123",
                  "object": "thread",
                  "created_at": 1699012949,
                  "metadata": {}
                }
                """;
    }

    @BeforeEach
    void setUp() {
        this.sender = new CreateThreadRequestSender(url, TIMEOUT, API_KEY);
        this.jsonConverter = new JsonConverter<>(CreateThreadRequest.class);
        this.builder = new CreateThreadRequestBuilder();
    }

    @Test
    public void shouldSendRequest() {
        var metadata = Map.of("key", "value");
        var request = builder
                .metadata(metadata)
                .addUserMessage("My message", Set.of("file-1", "file-2"), metadata)
                .build();

        var actualResponse = sender.sendRequest(request);
        var expectedResponse = new Thread("thread_123", "thread", Instant.ofEpochSecond(1699012949), Map.of());
        var expectedRequestBody = jsonConverter.convertRequestToJson(request);

        executeRequestAssertions(expectedRequestBody, 1, HttpMethod.POST, true);
        executeResponseAssertions(expectedResponse, actualResponse);
    }

}
