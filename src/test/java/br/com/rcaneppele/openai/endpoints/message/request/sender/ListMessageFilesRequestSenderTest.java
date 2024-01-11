package br.com.rcaneppele.openai.endpoints.message.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.QueryParameters;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.common.request.builder.QueryParametersBuilder;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.message.response.ListOfMessageFiles;
import br.com.rcaneppele.openai.endpoints.message.response.MessageFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

class ListMessageFilesRequestSenderTest extends BaseRequestSenderTest {

    private static final String THREAD_ID = "thread_123";
    private static final String MESSAGE_ID = "msg_123";

    private RequestSender<QueryParameters, ListOfMessageFiles> sender;
    private QueryParametersBuilder builder;

    @Override
    protected String expectedURI() {
        return "threads/" + THREAD_ID +"/messages/" +MESSAGE_ID +"/files?limit=1&order=asc&after=after_id&before=before_id";
    }

    @Override
    protected String mockJsonResponse() {
        return """
                {
                  "object": "list",
                  "data": [
                    {
                      "id": "file-123",
                      "object": "thread.message.file",
                      "created_at": 1699061776,
                      "message_id": "msg_123"
                    }
                  ],
                  "first_id": "file-123",
                  "last_id": "file-123",
                  "has_more": true
                }
                """;
    }

    @BeforeEach
    void setUp() {
        this.sender = new ListMessageFilesRequestSender(this.url, TIMEOUT, API_KEY, THREAD_ID, MESSAGE_ID);
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
        var expectedResponse = new ListOfMessageFiles(
                "list",
                "file-123",
                "file-123",
                true,
                List.of(
                        new MessageFile(
                                "file-123",
                                "thread.message.file",
                                Instant.ofEpochSecond(1699061776),
                                MESSAGE_ID
                        )
                ));

        executeRequestAssertions("", 1, HttpMethod.GET, true);
        executeResponseAssertions(expectedResponse, actualResponse);
    }

}