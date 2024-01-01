package br.com.rcaneppele.openai.endpoints.assistant.request.builder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateAssistantFileRequestBuilderTest {

    private CreateAssistantFileRequestBuilder builder;

    @BeforeEach
    void beforeEach() {
        this. builder = new CreateAssistantFileRequestBuilder();
    }

    @Test
    void shouldRejectNullAssistantId() {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.assistantId(null));
        assertEquals("Assistant id is required!", exception.getMessage());
    }

    @Test
    void shouldRejectEmptyAssistantId() {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.assistantId(""));
        assertEquals("Assistant id is required!", exception.getMessage());
    }

    @Test
    void shouldRejectNullFileId() {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.fileId(null));
        assertEquals("File id is required!", exception.getMessage());
    }

    @Test
    void shouldRejectEmptyFileId() {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.fileId(""));
        assertEquals("File id is required!", exception.getMessage());
    }

    @Test
    void shouldNotBuildWithoutRequiredFields() {
        assertThrows(IllegalArgumentException.class, () -> builder.build());
    }

    @Test
    void shouldNotBuildWithoutAssistantId() {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.fileId("file-123").build());
        assertEquals("Assistant id is required!", exception.getMessage());
    }

    @Test
    void shouldNotBuildWithoutFileId() {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.assistantId("asst_123").build());
        assertEquals("File id is required!", exception.getMessage());
    }

    @Test
    void shouldBuildWithAllParameters() {
        var assistantId = "asst_123";
        var fileId = "file-123";

        var request = builder
                .assistantId(assistantId)
                .fileId(fileId)
                .build();

        assertNotNull(request);
        assertEquals(assistantId, request.assistantId());
        assertEquals(fileId, request.fileId());
    }

}