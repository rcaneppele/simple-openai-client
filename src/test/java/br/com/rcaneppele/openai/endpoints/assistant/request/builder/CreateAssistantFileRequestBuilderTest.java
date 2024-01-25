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
    void shouldCallFileIdValidator() {
        var id = "file-id";
        builder.fileId(id);
        verify(idValidator).validateFileId(id);
    }

    @Test
    void shouldCallValidatorsOnBuild() {
        builder.build();
        verify(idValidator).validateFileId(any());
    }

    @Test
    void shouldBuildWithAllParameters() {
        var fileId = "file-123";

        var request = builder
                .fileId(fileId)
                .build();

        assertNotNull(request);
        assertEquals(fileId, request.fileId());
    }

}