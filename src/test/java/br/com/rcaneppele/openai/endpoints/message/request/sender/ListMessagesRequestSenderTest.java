package br.com.rcaneppele.openai.endpoints.message.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.QueryParameters;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.common.request.builder.QueryParametersBuilder;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.message.response.ListOfMessages;
import br.com.rcaneppele.openai.endpoints.message.response.Message;
import br.com.rcaneppele.openai.endpoints.message.response.MessageContent;
import br.com.rcaneppele.openai.endpoints.message.response.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

class ListMessagesRequestSenderTest extends BaseRequestSenderTest {

    private static final String THREAD_ID = "thread_123";

    private RequestSender<QueryParameters, ListOfMessages> sender;
    private QueryParametersBuilder builder;

    @Override
    protected String expectedURI() {
        return "threads/" + THREAD_ID +"/messages?limit=1&order=asc&after=after_id&before=before_id";
    }

    @Override
    protected String mockJsonResponse() {
        return """
                {
                  "object": "list",
                  "data": [
                    {
                      "id": "msg_123",
                      "object": "thread.message",
                      "created_at": 1699016383,
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
                  ],
                  "first_id": "msg_123",
                  "last_id": "msg_123",
                  "has_more": true
                }
                """;
    }

    @BeforeEach
    void setUp() {
        this.sender = new ListMessagesRequestSender(this.url, TIMEOUT, API_KEY, THREAD_ID);
        this.builder = new QueryParametersBuilder();
    }

    @Test
    protected void shouldSendRequest() {
        var request = builder
                .limit(1)
                .ascOrder()
                .after("after_id")
                .before("before_id")
                .build();

        var actualResponse = sender.sendRequest(request);
        var expectedResponse = new ListOfMessages(
                "list",
                "msg_123",
                "msg_123",
                true,
                List.of(
                        new Message(
                                "msg_123",
                                "thread.message",
                                Instant.ofEpochSecond(1699016383),
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
                                Map.of())
                ));

        executeRequestAssertions("", 1, HttpMethod.GET, true);
        executeResponseAssertions(expectedResponse, actualResponse);
    }

}