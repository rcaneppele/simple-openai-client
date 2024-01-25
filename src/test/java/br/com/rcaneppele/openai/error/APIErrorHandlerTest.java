package br.com.rcaneppele.openai.error;

import br.com.rcaneppele.openai.error.exception.*;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class APIErrorHandlerTest {

    private APIErrorHandler handler;

    @Mock
    private Response response;

    @Mock
    private ResponseBody responseBody;

    @BeforeEach
    void beforeEach() throws IOException {
        this.handler = new APIErrorHandler();
        var APIErrorJson = """
                    {
                      "error": {
                        "message": "error message",
                        "type": "invalid_request_error"
                      }
                   }
                """;
        given(response.body()).willReturn(responseBody);
        given(responseBody.string()).willReturn(APIErrorJson);
    }

    @Test
    void shouldHandleBadRequest() {
        given(response.code()).willReturn(400);

        var exception = assertThrows(BadRequestException.class, () -> handler.handleError(response));
        assertEquals("Bad Request error: error message", exception.getMessage());
    }

    @Test
    void shouldHandleAPIKeyError() {
        given(response.code()).willReturn(401);

        var exception = assertThrows(APIKeyException.class, () -> handler.handleError(response));
        assertEquals("API Key error: error message", exception.getMessage());
    }

    @Test
    void shouldHandleRateLimit() {
        given(response.code()).willReturn(429);

        var exception = assertThrows(RateLimitException.class, () -> handler.handleError(response));
        assertEquals("Rate Limit error: error message", exception.getMessage());
    }

    @Test
    void shouldHandleServiceUnavailable() {
        given(response.code()).willReturn(503);

        var exception = assertThrows(ServiceUnavailableException.class, () -> handler.handleError(response));
        assertEquals("Service Unavailable error: error message", exception.getMessage());
    }

    @Test
    void shouldHandleNotFoundError() {
        given(response.code()).willReturn(404);

        var exception = assertThrows(NotFoundException.class, () -> handler.handleError(response));
        assertEquals("Not Found error: error message", exception.getMessage());
    }

    @ParameterizedTest(name = "Testing handle for code {0}")
    @ValueSource(ints = {405, 406, 408, 415, 422, 500, 501, 502, 504, 522})
    void shouldHandleUnknownError(int code) {
        given(response.code()).willReturn(code);

        var exception = assertThrows(RuntimeException.class, () -> handler.handleError(response));
        assertEquals("Unknown error: error message", exception.getMessage());
    }

}
