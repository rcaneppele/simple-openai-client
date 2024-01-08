package br.com.rcaneppele.openai.endpoints.threads.request.builder;

import br.com.rcaneppele.openai.common.validation.MetadataValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ModifyThreadRequestBuilderTest {

    @InjectMocks
    private ModifyThreadRequestBuilder builder;

    @Mock
    private MetadataValidator metadataValidator;

    @Test
    void shouldValidateThreadId() {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.threadId(null));
        assertEquals("Thread id is required!", exception.getMessage());
    }

    @Test
    void shouldNotBuildWithoutThreadId() {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.build());
        assertEquals("Thread id is required!", exception.getMessage());
    }

    @Test
    void shouldCallMetadataValidator() {
        var metadata = Map.of("key", "value");
        builder.metadata(metadata);
        verify(metadataValidator).validate(metadata);
    }

    @Test
    void shouldBuildWithAllParameters() {
        var threadId = "thread_123";
        var metadata = Map.of("key1", "value-2", "key-2", "value-2");

        var request = builder
                .threadId(threadId)
                .metadata(metadata)
                .build();

        assertEquals(threadId, request.threadId());
        assertEquals(metadata, request.metadata());
    }

}