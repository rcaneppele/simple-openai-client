package br.com.rcaneppele.openai.endpoints.message.request.sender;

import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.message.request.CreateMessageRequest;
import br.com.rcaneppele.openai.endpoints.message.request.builder.CreateMessageRequestBuilder;
import br.com.rcaneppele.openai.endpoints.message.response.Message;
import br.com.rcaneppele.openai.endpoints.message.response.MessageContent;
import br.com.rcaneppele.openai.endpoints.message.response.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

class CreateMessageRequestSenderTest extends BaseRequestSenderTest {

    private static final String THREAD_ID = "thread_123";

    private RequestSender<CreateMessageRequest, Message> sender;
    private JsonConverter<CreateMessageRequest> jsonConverter;
    private CreateMessageRequestBuilder builder;

    @Override
    protected String expectedURI() {
        return "threads/" + THREAD_ID +"/messages";
    }

    @Override
    protected String mockJsonResponse() {
        return """
                {
                  "id": "msg_123",
                  "object": "thread.message",
                  "created_at": 1699017614,
                  "thread_id": "thread_123",
                  "role": "user",
                  "content": [
                    {
                      "type": "text",
                      "text": {
                        "value": "How does AI work? Explain it in simple terms.",
                        "annotations": []
                      }
                    }
                  ],
                  "file_ids": [],
                  "assistant_id": null,
                  "run_id": null,
                  "metadata": {}
                }
                """;
    }

    @BeforeEach
    void setUp() {
        this.sender = new CreateMessageRequestSender(this.url, TIMEOUT, API_KEY, THREAD_ID);
        this.jsonConverter = new JsonConverter<>(CreateMessageRequest.class);
        this.builder = new CreateMessageRequestBuilder();
    }

    @Test
    public void shouldSendRequest() {
        var request = builder
                .threadId(THREAD_ID)
                .content("How does AI work? Explain it in simple terms.")
                .build();

        var actualResponse = sender.sendRequest(request);
        var expectedResponse = new Message(
                "msg_123",
                "thread.message",
                Instant.ofEpochSecond(1699017614),
                THREAD_ID,
                "user",
                List.of(
                        new MessageContent(
                            "text",
                            new Text("How does AI work? Explain it in simple terms.", List.of()),
                            null
                        )
                ),
                null,
                null,
                Set.of(),
                Map.of());
        var expectedRequestBody = jsonConverter.convertRequestToJson(request);
        executeRequestAssertions(expectedRequestBody, 1, HttpMethod.POST, true);
        executeResponseAssertions(expectedResponse, actualResponse);
    }

}