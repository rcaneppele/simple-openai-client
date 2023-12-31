package br.com.rcaneppele.openai.assistant.response;

import br.com.rcaneppele.openai.assistant.tools.Tool;
import br.com.rcaneppele.openai.assistant.tools.ToolType;
import br.com.rcaneppele.openai.error.exception.APIKeyException;
import br.com.rcaneppele.openai.error.exception.BadRequestException;
import br.com.rcaneppele.openai.error.exception.RateLimitException;
import br.com.rcaneppele.openai.error.exception.ServiceUnavailableException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CreateAssistantResponseBuilderTest {

    private MockWebServer server;
    private CreateAssistantResponseBuilder builder;

    @BeforeEach
    void setUp() throws IOException {
        this.server = new MockWebServer();
        this.server.start();
        this.builder = new CreateAssistantResponseBuilder();
    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
    }

    @ParameterizedTest(name = "Testing error handling for code: {0}")
    @ValueSource(ints = {400, 401, 429, 503, 500})
    void shouldHandleErrorResponse(int code) throws IOException {
        var mockResponse = new MockResponse()
                .setResponseCode(code)
                .setBody("""
                        {
                            "error": {
                                "message": "error message",
                                "type": "error type",
                                "code": "%d"
                            }
                        }
                        """.formatted(code));
        server.enqueue(mockResponse);
        var httpResponse = sendRequest();

        switch (code) {
            case 400 -> {
                var exception = assertThrows(BadRequestException.class, () -> builder.build(httpResponse));
                assertEquals("Bad Request error: error message", exception.getMessage());
            }
            case 401 -> {
                var exception = assertThrows(APIKeyException.class, () -> builder.build(httpResponse));
                assertEquals("API Key error: error message", exception.getMessage());
            }
            case 429 -> {
                var exception = assertThrows(RateLimitException.class, () -> builder.build(httpResponse));
                assertEquals("Rate Limit error: error message", exception.getMessage());
            }
            case 503 -> {
                var exception = assertThrows(ServiceUnavailableException.class, () -> builder.build(httpResponse));
                assertEquals("Service Unavailable error: error message", exception.getMessage());
            }
            default -> {
                var exceptionDefault = assertThrows(RuntimeException.class, () -> builder.build(httpResponse));
                assertEquals("Unknown error: error message", exceptionDefault.getMessage());
            }
        }
    }

    @Test
    void shouldBuildCreateAssistantResponse() throws IOException {
        var mockResponse = new MockResponse()
                .setResponseCode(200)
                .setBody("""
                        {
                          "id": "asst_abc123",
                          "object": "assistant",
                          "created_at": 1677652288,
                          "model": "gpt-4-1106-preview",
                          "name": "Math Tutor",
                          "description": "Description",
                          "instructions": "You are a personal math tutor",
                          "file_ids": ["file-123"],
                          "tools": [
                            {
                              "type": "retrieval"
                            }
                          ]
                        }
                        """);
        server.enqueue(mockResponse);
        var httpResponse = sendRequest();

        var response = builder.build(httpResponse);

        assertNotNull(response);

        assertEquals("asst_abc123", response.id());
        assertEquals("assistant", response.object());
        assertEquals(Instant.ofEpochSecond(1677652288), response.createdAt());
        assertEquals("gpt-4-1106-preview", response.model());
        assertEquals("Math Tutor", response.name());
        assertEquals("Description", response.description());
        assertEquals("You are a personal math tutor", response.instructions());
        assertEquals(Set.of("file-123"), response.fileIds());
        assertEquals(1, response.tools().size());
        assertTrue(response.tools().contains(
                new Tool(ToolType.RETRIEVAL.getName(), null)
        ));
    }

    private Response sendRequest() throws IOException {
        var request = new Request.Builder()
                .url(server.url("/test/assistants"))
                .build();

        return new OkHttpClient().newCall(request).execute();
    }

}