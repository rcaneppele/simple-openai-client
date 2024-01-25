package br.com.rcaneppele.openai.endpoints.thread.request.builder;

import br.com.rcaneppele.openai.common.validation.MetadataValidator;
import br.com.rcaneppele.openai.endpoints.thread.request.ModifyThreadRequest;
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
class ModifyThreadRequestBuilderTest {

    @InjectMocks
    private ModifyThreadRequestBuilder builder;

    @Mock
    private MetadataValidator metadataValidator;

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

        var actual = builder
                .metadata(metadata)
                .build();

        var expected = new ModifyThreadRequest(metadata);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

}