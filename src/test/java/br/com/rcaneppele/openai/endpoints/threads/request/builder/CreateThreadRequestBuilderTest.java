package br.com.rcaneppele.openai.endpoints.threads.request.builder;

import br.com.rcaneppele.openai.common.validation.MetadataValidator;
import br.com.rcaneppele.openai.endpoints.threads.messages.request.CreateMessageRequest;
import br.com.rcaneppele.openai.endpoints.threads.request.CreateThreadRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
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
        var role = "user";
        var message1 = new CreateMessageRequest(role, "message 1", null, null);
        var message2 = new CreateMessageRequest(role, "message 2", Set.of("file-1", "file-2"), null);
        var message3 = new CreateMessageRequest(role, "message 3", null, metadata);
        var message4 = new CreateMessageRequest(role, "message 4", Set.of("file-3"), metadata);

        var actual = builder
                .addUserMessage(message1.content(), null, null)
                .addUserMessage(message2.content(), message2.fileIds(), null)
                .addUserMessage(message3.content(), null, message3.metadata())
                .addUserMessage(message4.content(), message4.fileIds(), message4.metadata())
                .metadata(metadata)
                .build();

        var expected = new CreateThreadRequest(List.of(message1, message2, message3, message4), metadata);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

}