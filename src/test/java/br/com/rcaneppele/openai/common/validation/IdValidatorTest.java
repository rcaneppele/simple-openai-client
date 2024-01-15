package br.com.rcaneppele.openai.common.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class IdValidatorTest {

    private static final String ASSISTANT_ID_MESSAGE = "Assistant id is required!";
    private static final String FILE_ID_MESSAGE = "File id is required!";
    private static final String THREAD_ID_MESSAGE = "Thread id is required!";
    private static final String MESSAGE_ID_MESSAGE = "Message id is required!";
    private static final String RUN_ID_MESSAGE = "Run id is required!";

    private IdValidator validator;

    @BeforeEach
    void setup() {
        this.validator = new IdValidator();
    }

    @Test
    void shouldRejectNullAssistantId() {
        var exception = assertThrows(IllegalArgumentException.class, () -> validator.validateAssistantId(null));
        assertEquals(ASSISTANT_ID_MESSAGE, exception.getMessage());
    }

    @ParameterizedTest(name = "Testing Assistant id with empty String")
    @ValueSource(strings = {"", " "})
    void shouldRejectEmptyAssistantId(String id) {
        var exception = assertThrows(IllegalArgumentException.class, () -> validator.validateAssistantId(id));
        assertEquals(ASSISTANT_ID_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldRejectNullFileId() {
        var exception = assertThrows(IllegalArgumentException.class, () -> validator.validateFileId(null));
        assertEquals(FILE_ID_MESSAGE, exception.getMessage());
    }

    @ParameterizedTest(name = "Testing File id with empty String")
    @ValueSource(strings = {"", " "})
    void shouldRejectEmptyFileId(String id) {
        var exception = assertThrows(IllegalArgumentException.class, () -> validator.validateFileId(id));
        assertEquals(FILE_ID_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldRejectNullThreadId() {
        var exception = assertThrows(IllegalArgumentException.class, () -> validator.validateThreadId(null));
        assertEquals(THREAD_ID_MESSAGE, exception.getMessage());
    }

    @ParameterizedTest(name = "Testing Thread id with empty String")
    @ValueSource(strings = {"", " "})
    void shouldRejectEmptyThreadId(String id) {
        var exception = assertThrows(IllegalArgumentException.class, () -> validator.validateThreadId(id));
        assertEquals(THREAD_ID_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldRejectNullMessageId() {
        var exception = assertThrows(IllegalArgumentException.class, () -> validator.validateMessageId(null));
        assertEquals(MESSAGE_ID_MESSAGE, exception.getMessage());
    }

    @ParameterizedTest(name = "Testing Message id with empty String")
    @ValueSource(strings = {"", " "})
    void shouldRejectEmptyMessageId(String id) {
        var exception = assertThrows(IllegalArgumentException.class, () -> validator.validateMessageId(id));
        assertEquals(MESSAGE_ID_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldRejectNullRunId() {
        var exception = assertThrows(IllegalArgumentException.class, () -> validator.validateRunId(null));
        assertEquals(RUN_ID_MESSAGE, exception.getMessage());
    }

    @ParameterizedTest(name = "Testing Run id with empty String")
    @ValueSource(strings = {"", " "})
    void shouldRejectEmptyRunId(String id) {
        var exception = assertThrows(IllegalArgumentException.class, () -> validator.validateRunId(id));
        assertEquals(RUN_ID_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldNotRejectValidId() {
        var validId = "id";
        assertDoesNotThrow(() -> validator.validateAssistantId(validId));
        assertDoesNotThrow(() -> validator.validateFileId(validId));
        assertDoesNotThrow(() -> validator.validateThreadId(validId));
    }

}