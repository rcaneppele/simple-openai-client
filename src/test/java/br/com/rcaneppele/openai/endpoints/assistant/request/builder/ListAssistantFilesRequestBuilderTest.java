package br.com.rcaneppele.openai.endpoints.assistant.request.builder;

import br.com.rcaneppele.openai.endpoints.assistant.request.ListAssistantFilesRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ListAssistantFilesRequestBuilderTest {

    private ListAssistantFilesRequestBuilder builder;

    @BeforeEach
    void beforeEach() {
        this. builder = new ListAssistantFilesRequestBuilder();
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
    void shouldNotBuildWithoutRequiredFields() {
        assertThrows(IllegalArgumentException.class, () -> builder.build());
    }

    @ParameterizedTest
    @ValueSource(ints = {-10, 0, 101, 110})
    void shouldValidateLimit(int limit) {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.limit(limit));
        assertEquals("Limit must be between 1 and 100!", exception.getMessage());
    }

    @Test
    void shouldBuildWithDefaultParameterValues() {
        var request = builder.assistantId("asst_123").build();

        assertNotNull(request);
        assertEquals(20, request.limit());
        assertEquals("desc", request.order());
        assertNull(request.after());
        assertNull(request.before());
    }

    @Test
    void shouldBuildWithAscOrder() {
        var request = builder
                .assistantId("asst_123")
                .ascOrder()
                .build();

        assertNotNull(request);
        assertEquals("asc", request.order());
    }

    @Test
    void shouldBuildWithAllParameters() {
        var assistantId = "asst_123";
        var limit = 50;
        var after = "after-id";
        var before = "before-id";

        var request = (ListAssistantFilesRequest) builder
                .assistantId(assistantId)
                .limit(limit)
                .after(after)
                .before(before)
                .ascOrder()
                .build();

        assertNotNull(request);
        assertEquals(assistantId, request.assistantId());
        assertEquals(limit, request.limit());
        assertEquals(after, request.after());
        assertEquals(before, request.before());
        assertEquals("asc", request.order());
    }

}