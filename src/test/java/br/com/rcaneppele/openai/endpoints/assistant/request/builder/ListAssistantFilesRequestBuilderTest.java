package br.com.rcaneppele.openai.endpoints.assistant.request.builder;

import br.com.rcaneppele.openai.common.validation.IdValidator;
import br.com.rcaneppele.openai.endpoints.assistant.request.ListAssistantFilesRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ListAssistantFilesRequestBuilderTest {

    @InjectMocks
    private ListAssistantFilesRequestBuilder builder;

    @Mock
    private IdValidator idValidator;

    @Test
    void shouldCallAssistantIdValidator() {
        var id = "assistant-id";
        builder.assistantId(id);
        verify(idValidator).validateAssistantId(id);
    }

    @Test
    void shouldCallValidatorsOnBuild() {
        builder.build();
        verify(idValidator).validateAssistantId(any());
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