package br.com.rcaneppele.openai.endpoints.message.request.builder;

import br.com.rcaneppele.openai.common.validation.IdValidator;
import br.com.rcaneppele.openai.common.validation.MetadataValidator;
import br.com.rcaneppele.openai.endpoints.message.request.ModifyMessageRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ModifyMessageRequestBuilderTest {

    @InjectMocks
    private ModifyMessageRequestBuilder builder;

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
    void shouldCallMessageIdValidator() {
        var messageId = "message-id";
        builder.messageId(messageId);
        verify(idValidator).validateMessageId(messageId);
    }

    @Test
    void shouldCallMetadataValidator() {
        var metadata = Map.of("key", "value");
        builder.metadata(metadata);
        verify(metadataValidator).validate(metadata);
    }

    @Test
    void shouldCallThreadIdValidatorOnBuild() {
        builder.build();
        verify(idValidator).validateThreadId(null);
    }

    @Test
    void shouldCallMessageIdValidatorOnBuild() {
        builder.build();
        verify(idValidator).validateMessageId(null);
    }

    @Test
    void shouldBuildWithAllParameters() {
        var metadata = Map.of("key", "value");

        var actual = builder
                .threadId("thread-id")
                .messageId("message-id")
                .metadata(metadata)
                .build();

        var expected = new ModifyMessageRequest(
                "thread-id",
                "message-id",
                metadata
        );

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

}
