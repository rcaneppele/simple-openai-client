package br.com.rcaneppele.openai.endpoints.run.request.builder;

import br.com.rcaneppele.openai.common.validation.MetadataValidator;
import br.com.rcaneppele.openai.endpoints.run.request.ModifyRunRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ModifyRunRequestBuilderTest {

    @InjectMocks
    private ModifyRunRequestBuilder builder;

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
        var metadata = Map.of("key", "value");

        var actual = builder.metadata(metadata).build();
        var expected = new ModifyRunRequest(metadata);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

}
