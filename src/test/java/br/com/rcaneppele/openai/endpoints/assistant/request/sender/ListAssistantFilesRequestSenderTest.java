package br.com.rcaneppele.openai.endpoints.assistant.request.sender;

import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.QueryParameters;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.common.request.builder.QueryParametersBuilder;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.assistant.response.AssistantFile;
import br.com.rcaneppele.openai.endpoints.assistant.response.ListOfAssistantFiles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

class ListAssistantFilesRequestSenderTest extends BaseRequestSenderTest {

    private static final String ASSISTANT_ID = "asst_123";

    private RequestSender<QueryParameters, ListOfAssistantFiles> sender;
    private QueryParametersBuilder builder;

    @Override
    protected String expectedURI() {
        return "assistants/" +ASSISTANT_ID +"/files?limit=1&order=asc&after=after_id&before=before_id";
    }

    @Override
    protected String mockJsonResponse() {
        return """
                {
                  "object": "list",
                  "data": [
                    {
                      "id": "file-123",
                      "object": "assistant.file",
                      "created_at": 1699060412,
                      "assistant_id": "asst_123"
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
        this.sender = new ListAssistantFilesRequestSender(this.url, TIMEOUT, API_KEY, ASSISTANT_ID);
        this.builder = new QueryParametersBuilder();
    }

    @Test
    public void shouldSendRequest() {
        var request = builder
                .limit(1)
                .ascOrder()
                .after("after_id")
                .before("before_id")
                .build();

        var actualResponse = sender.sendRequest(request);
        var expectedResponse = new ListOfAssistantFiles(
                "list",
                "file-123",
                "file-123",
                true,
                List.of(
                        new AssistantFile(
                                "file-123",
                                "assistant.file",
                                Instant.ofEpochSecond(1699060412),
                                ASSISTANT_ID)
                ));

        executeRequestAssertions("", 1, HttpMethod.GET, true);
        executeResponseAssertions(expectedResponse, actualResponse);
    }

}