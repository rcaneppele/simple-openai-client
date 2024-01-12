package br.com.rcaneppele.openai.endpoints.assistant.request.sender;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.common.request.HttpMethod;
import br.com.rcaneppele.openai.common.request.QueryParameters;
import br.com.rcaneppele.openai.common.request.RequestSender;
import br.com.rcaneppele.openai.common.request.builder.QueryParametersBuilder;
import br.com.rcaneppele.openai.endpoints.BaseRequestSenderTest;
import br.com.rcaneppele.openai.endpoints.assistant.response.Assistant;
import br.com.rcaneppele.openai.endpoints.assistant.response.ListOfAssistants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

class ListAssistantsRequestSenderTest extends BaseRequestSenderTest {

    private RequestSender<QueryParameters, ListOfAssistants> sender;
    private QueryParametersBuilder builder;

    @Override
    protected String expectedURI() {
        return "assistants?limit=1&order=asc&after=after_id&before=before_id";
    }

    @Override
    protected String mockJsonResponse() {
        return """
                {
                  "object": "list",
                  "data": [
                    {
                      "id": "asst_123",
                      "object": "assistant",
                      "created_at": 1698982736,
                      "name": "Coding Tutor",
                      "description": null,
                      "model": "gpt-4",
                      "instructions": "You are a helpful assistant designed to make me better at coding!",
                      "tools": [],
                      "file_ids": [],
                      "metadata": {}
                    }
                  ],
                  "first_id": "asst_123",
                  "last_id": "asst_123",
                  "has_more": true
                }
                """;
    }

    @BeforeEach
    void setUp() {
        this.sender = new ListAssistantsRequestSender(this.url, TIMEOUT, API_KEY);
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
        var expectedResponse = new ListOfAssistants(
                "list",
                "asst_123",
                "asst_123",
                true,
                List.of(
                        new Assistant(
                                "asst_123",
                                "assistant",
                                Instant.ofEpochSecond(1698982736),
                                "Coding Tutor",
                                null,
                                OpenAIModel.GPT_4,
                                "You are a helpful assistant designed to make me better at coding!",
                                Set.of(),
                                Set.of(),
                                Map.of())
                ));

        executeRequestAssertions("", 1, HttpMethod.GET, true);
        executeResponseAssertions(expectedResponse, actualResponse);
    }

}