package br.com.rcaneppele.openai.endpoints.assistant.request.builder;

import br.com.rcaneppele.openai.common.validation.IdValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateAssistantFileRequestBuilderTest {

    @InjectMocks
    private CreateAssistantFileRequestBuilder builder;

    @Mock
    private IdValidator idValidator;

    @Test
    void shouldCallAssistantIdValidator() {
        var id = "assistant-id";
        builder.assistantId(id);
        verify(idValidator).validateAssistantId(id);
    }

    @Test
    void shouldCallFileIdValidator() {
        var id = "file-id";
        builder.fileId(id);
        verify(idValidator).validateFileId(id);
    }

    @Test
    void shouldCallValidatorsOnBuild() {
        builder.build();
        verify(idValidator).validateAssistantId(any());
        verify(idValidator).validateFileId(any());
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