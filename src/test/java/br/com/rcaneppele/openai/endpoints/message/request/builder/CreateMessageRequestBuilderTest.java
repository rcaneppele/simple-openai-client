package br.com.rcaneppele.openai.endpoints.message.request.builder;

import br.com.rcaneppele.openai.common.validation.IdValidator;
import br.com.rcaneppele.openai.common.validation.MetadataValidator;
import br.com.rcaneppele.openai.endpoints.message.request.CreateMessageRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateMessageRequestBuilderTest {

    public static final String REQUIRED_CONTENT_MESSAGE = "The content of the message is required!";

    @InjectMocks
    private CreateMessageRequestBuilder builder;

    @Mock
    private MetadataValidator metadataValidator;

    @Mock
    private IdValidator idValidator;

    @Test
    void shouldCallThreadIdValidator() {
        var threadId = "thread-id";
        builder.threadId(threadId);
        verify(idValidator).validateThreadId(threadId);
    }

    @Test
    void shouldNotAddNullContent() {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.content(null));
        assertEquals(REQUIRED_CONTENT_MESSAGE, exception.getMessage());
    }

    @ParameterizedTest(name = "Testing empty content")
    @ValueSource(strings = {"", " "})
    void shouldNotAddEmptyContent(String content) {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.content(content));
        assertEquals(REQUIRED_CONTENT_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldCallMetadataValidator() {
        var metadata = Map.of("key", "value");
        builder.metadata(metadata);
        verify(metadataValidator).validate(metadata);
    }

    @Test
    void shouldNotAddMoreThan10FileIds() {
        var fileIds = new String[]{"file-1", "file-2", "file-3", "file-4", "file-5", "file-6", "file-7", "file-8", "file-9", "file-10", "file-11"};
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.fileIds(fileIds));
        assertEquals("There can be a maximum of 10 files attached to a message!", exception.getMessage());
    }

    @Test
    void shouldCallThreadIdValidatorOnBuild() {
        builder.content("content").build();
        verify(idValidator).validateThreadId(null);
    }

    @Test
    void shouldValidateContentOnBuild() {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.threadId("thread-id").build());
        assertEquals(REQUIRED_CONTENT_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldBuildWithAllParameters() {
        var metadata = Map.of("key", "value");

        var actual = builder
                .threadId("thread-id")
                .content("content")
                .fileIds("fileId-1", "fileId-2")
                .metadata(metadata)
                .build();

        var expected = new CreateMessageRequest(
                "thread-id",
                "user",
                "content",
                Set.of("fileId-1", "fileId-2"),
                metadata
        );

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

}