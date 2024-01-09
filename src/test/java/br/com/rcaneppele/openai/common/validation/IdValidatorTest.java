package br.com.rcaneppele.openai.common.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class IdValidatorTest {

    private static final String ASSISTANT_MESSAGE = "Assistant id is required!";
    private static final String FILE_MESSAGE = "File id is required!";
    private static final String THREAD_MESSAGE = "Thread id is required!";

    private IdValidator validator;

    @BeforeEach
    void setup() {
        this.validator = new IdValidator();
    }

    @Test
    void shouldRejectNullAssistantId() {
        var exception = assertThrows(IllegalArgumentException.class, () -> validator.validateAssistantId(null));
        assertEquals(ASSISTANT_MESSAGE, exception.getMessage());
    }

    @ParameterizedTest(name = "Testing Assistant id with empty String")
    @ValueSource(strings = {"", " "})
    void shouldRejectEmptyAssistantId(String id) {
        var exception = assertThrows(IllegalArgumentException.class, () -> validator.validateAssistantId(id));
        assertEquals(ASSISTANT_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldRejectNullFileId() {
        var exception = assertThrows(IllegalArgumentException.class, () -> validator.validateFileId(null));
        assertEquals(FILE_MESSAGE, exception.getMessage());
    }

    @ParameterizedTest(name = "Testing File id with empty String")
    @ValueSource(strings = {"", " "})
    void shouldRejectEmptyFileId(String id) {
        var exception = assertThrows(IllegalArgumentException.class, () -> validator.validateFileId(id));
        assertEquals(FILE_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldRejectNullThreadId() {
        var exception = assertThrows(IllegalArgumentException.class, () -> validator.validateThreadId(null));
        assertEquals(THREAD_MESSAGE, exception.getMessage());
    }

    @ParameterizedTest(name = "Testing Thread id with empty String")
    @ValueSource(strings = {"", " "})
    void shouldRejectEmptyThreadId(String id) {
        var exception = assertThrows(IllegalArgumentException.class, () -> validator.validateThreadId(id));
        assertEquals(THREAD_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldNotRejectValidId() {
        var validId = "id";
        assertDoesNotThrow(() -> validator.validateAssistantId(validId));
        assertDoesNotThrow(() -> validator.validateFileId(validId));
        assertDoesNotThrow(() -> validator.validateThreadId(validId));
    }

}