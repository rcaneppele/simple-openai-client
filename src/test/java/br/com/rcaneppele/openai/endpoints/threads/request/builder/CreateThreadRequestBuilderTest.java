package br.com.rcaneppele.openai.endpoints.threads.request.builder;

import br.com.rcaneppele.openai.common.validation.MetadataValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class CreateThreadRequestBuilderTest {

    @InjectMocks
    private CreateThreadRequestBuilder builder;

    @Mock
    private MetadataValidator metadataValidator;

    @Test
    void shouldNotAddMessageWithNullContent() {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.addUserMessage(null, null, null));
        assertEquals("The content of the message is required!", exception.getMessage());
    }

    @Test
    void shouldNotAddMessageWithEmptyContent() {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.addUserMessage("", null, null));
        assertEquals("The content of the message is required!", exception.getMessage());
    }

    @Test
    void shouldNotAddMessageWithMoreThan10FileIds() {
        var fileIds = Set.of("file-1", "file-2", "file-3", "file-4", "file-5", "file-6", "file-7", "file-8", "file-9", "file-10", "file-11");
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.addUserMessage("message", fileIds, null));
        assertEquals("There can be a maximum of 10 files attached to a message!", exception.getMessage());
    }

    @Test
    void shouldCallMetadataValidator() {
        var metadata = Map.of("key", "value");
        builder.metadata(metadata);
        verify(metadataValidator).validate(metadata);
    }

    @Test
    void shouldCallMetadataValidatorForMessageMetadata() {
        var metadata = Map.of("key", "value");
        builder.addUserMessage("message", null, metadata);
        verify(metadataValidator).validate(metadata);
    }

    @Test
    void shouldBuildWithNoParameters() {
        var request = builder.build();

        verifyNoInteractions(metadataValidator);
        assertTrue(request.messages().isEmpty());
        assertNull(request.metadata());
    }

    @Test
    void shouldBuildWithAllParameters() {
        var metadata = Map.of("key", "value");
        var request = builder
                .addUserMessage("message 1", null, null)
                .addUserMessage("message 2", Set.of("file-1", "file-2"), null)
                .addUserMessage("message 3", null, metadata)
                .addUserMessage("message 4", Set.of("file-3"), metadata)
                .metadata(metadata)
                .build();

        assertEquals(metadata, request.metadata());
        assertEquals(4, request.messages().size());

        assertEquals("message 1", request.messages().get(0).content());
        assertNull(request.messages().get(0).fileIds());
        assertNull(request.messages().get(0).metadata());

        assertEquals("message 2", request.messages().get(1).content());
        assertEquals(2, request.messages().get(1).fileIds().size());
        assertTrue(request.messages().get(1).fileIds().containsAll(Set.of("file-1", "file-2")));
        assertNull(request.messages().get(1).metadata());

        assertEquals("message 3", request.messages().get(2).content());
        assertNull(request.messages().get(2).fileIds());
        assertEquals(metadata, request.messages().get(2).metadata());

        assertEquals("message 4", request.messages().get(3).content());
        assertEquals(1, request.messages().get(3).fileIds().size());
        assertTrue(request.messages().get(3).fileIds().containsAll(Set.of("file-3")));
        assertEquals(metadata, request.messages().get(3).metadata());
    }

}