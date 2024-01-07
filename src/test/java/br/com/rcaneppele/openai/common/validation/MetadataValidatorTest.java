package br.com.rcaneppele.openai.common.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MetadataValidatorTest {

    private MetadataValidator validator;

    @BeforeEach
    void setup() {
        this.validator = new MetadataValidator();
    }

    @Test
    void shouldValidateMaximumLength() {
        var metadata = new HashMap<String, String>();
        for (int i = 1; i <= 16; i++) {
            var actual = String.valueOf(i);
            metadata.put(actual, actual);
            assertDoesNotThrow(() -> validator.validate(metadata));
        }

        metadata.put("17", "17");
        var exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(metadata));
        assertEquals("There can be a maximum of 16 key-value pairs that can be attached to the assistant!", exception.getMessage());
    }

    @Test
    void shouldValidateKeyLength() {
        var invalidKey = "a".repeat(65);
        var exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(Map.of(invalidKey, "value")));
        assertEquals("Metadata Keys can be a maximum of 64 characters long!", exception.getMessage());
    }

    @Test
    void shouldValidateValueLength() {
        var invalidValue = "a".repeat(513);
        var exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(Map.of("key", invalidValue)));
        assertEquals("Metadata Values can be a maximum of 512 characters long!", exception.getMessage());
    }

}