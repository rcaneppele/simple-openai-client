package br.com.rcaneppele.openai.chatcompletion.response;

import br.com.rcaneppele.openai.common.message.ChatCompletionChoice;
import br.com.rcaneppele.openai.common.message.ChatMessage;
import br.com.rcaneppele.openai.error.exception.APIKeyException;
import br.com.rcaneppele.openai.error.exception.BadRequestException;
import br.com.rcaneppele.openai.error.exception.RateLimitException;
import br.com.rcaneppele.openai.error.exception.ServiceUnavailableException;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.schedulers.TestScheduler;
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
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ChatCompletionResponseBuilderTest {

    private MockWebServer server;
    private ChatCompletionResponseBuilder builder;

    @BeforeEach
    void setUp() throws IOException {
        this.server = new MockWebServer();
        this.server.start();
        this.builder = new ChatCompletionResponseBuilder();
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
    void shouldBuildChatCompletionResponse() throws IOException {
        var mockResponse = new MockResponse()
                .setResponseCode(200)
                .setBody("""
                        {
                          "id": "chatcmpl-123",
                          "object": "chat.completion",
                          "created": 1677652288,
                          "model": "gpt-4-1106-preview",
                          "system_fingerprint": "fp_44709d6fcb",
                          "choices": [{
                            "index": 0,
                            "message": {
                              "role": "assistant",
                              "content": "\\n\\nHello there, how may I assist you today?"
                            },
                            "logprobs": null,
                            "finish_reason": "stop"
                          }],
                          "usage": {
                            "prompt_tokens": 9,
                            "completion_tokens": 12,
                            "total_tokens": 21
                          }
                        }
                        """);
        server.enqueue(mockResponse);
        var httpResponse = sendRequest();

        var chatCompletionResponse = builder.build(httpResponse);

        assertNotNull(chatCompletionResponse);

        assertEquals("chatcmpl-123", chatCompletionResponse.id());
        assertEquals("chat.completion", chatCompletionResponse.object());
        assertEquals(Instant.ofEpochSecond(1677652288), chatCompletionResponse.created());
        assertEquals("gpt-4-1106-preview", chatCompletionResponse.model());
        assertEquals("fp_44709d6fcb", chatCompletionResponse.systemFingerprint());

        assertEquals(1, chatCompletionResponse.choices().size());
        assertEquals(0, chatCompletionResponse.choices().get(0).index());
        assertEquals("assistant", chatCompletionResponse.choices().get(0).message().role());
        assertEquals("\n\nHello there, how may I assist you today?", chatCompletionResponse.choices().get(0).message().content());
        assertNull(chatCompletionResponse.choices().get(0).logProbs());
        assertEquals("stop", chatCompletionResponse.choices().get(0).finishReason());

        assertEquals(9, chatCompletionResponse.usage().promptTokens());
        assertEquals(12, chatCompletionResponse.usage().completionTokens());
        assertEquals(21, chatCompletionResponse.usage().totalTokens());
    }

    @Test
    void shouldBuildStreamChatCompletionResponse() throws IOException {
        var mockResponse = new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "text/event-stream")
                .setBody("""
                        data: {"id": "chatcmpl-123","object": "chat.completion.chunk","created": 1677652288,"model": "gpt-4-1106-preview","choices": [{"index": 0,"delta": {"content": "Hello"}}]}
                        data: {"id": "chatcmpl-456","object": "chat.completion.chunk","created": 1677652289,"model": "gpt-4-1106-preview","choices": [{"index": 0,"delta": {"content": " "}}]}
                        data: {"id": "chatcmpl-789","object": "chat.completion.chunk","created": 1677652290,"model": "gpt-4-1106-preview","choices": [{"index": 0,"delta": {"content": "there!"}}]}
                        data: [DONE]
                        """);
        server.enqueue(mockResponse);
        var httpResponse = sendRequest();
        var testObserver = new TestObserver<ChatCompletionResponse>();
        var scheduler = new TestScheduler();

        Observable<ChatCompletionResponse> observable = Observable.create(emitter -> {
            builder.buildForStream(httpResponse, emitter);
        });

        observable.observeOn(scheduler).subscribe(testObserver);
        scheduler.advanceTimeBy(1, TimeUnit.SECONDS);

        testObserver.assertValues(
            createChatCompletionResponse("chatcmpl-123", 1677652288, "Hello"),
            createChatCompletionResponse("chatcmpl-456", 1677652289, " "),
            createChatCompletionResponse("chatcmpl-789", 1677652290, "there!")
        );

        testObserver.assertComplete();
        testObserver.assertNoErrors();
    }

    private Response sendRequest() throws IOException {
        var request = new Request.Builder()
                .url(server.url("/test/chat/completions"))
                .build();

        return new OkHttpClient().newCall(request).execute();
    }

    private ChatCompletionResponse createChatCompletionResponse(String id, int epochSecond, String content) {
        return new ChatCompletionResponse(
                id,
                "chat.completion.chunk",
                Instant.ofEpochSecond(epochSecond),
                "gpt-4-1106-preview",
                null,
                Arrays.asList(new ChatCompletionChoice(
                        0l,
                        new ChatMessage(null, content),
                        null,
                        null
                )),
                null
        );
    }

}
