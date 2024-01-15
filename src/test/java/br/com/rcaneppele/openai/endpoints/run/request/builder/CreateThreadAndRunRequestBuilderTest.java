package br.com.rcaneppele.openai.endpoints.run.request.builder;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.common.validation.IdValidator;
import br.com.rcaneppele.openai.common.validation.MetadataValidator;
import br.com.rcaneppele.openai.endpoints.assistant.tools.Function;
import br.com.rcaneppele.openai.endpoints.assistant.tools.Tool;
import br.com.rcaneppele.openai.endpoints.assistant.tools.ToolType;
import br.com.rcaneppele.openai.endpoints.run.request.CreateThreadAndRunRequest;
import br.com.rcaneppele.openai.endpoints.thread.request.builder.CreateThreadRequestBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateThreadAndRunRequestBuilderTest {

    @InjectMocks
    private CreateThreadAndRunRequestBuilder builder;

    @Mock
    private MetadataValidator metadataValidator;

    @Mock
    private IdValidator idValidator;

    @Test
    void shouldCallAssistantIdValidator() {
        var asssistantId = "asssistant-id";
        builder.assistantId(asssistantId);
        verify(idValidator).validateAssistantId(asssistantId);
    }

    @Test
    void shouldCallMetadataValidator() {
        var metadata = Map.of("key", "value");
        builder.metadata(metadata);
        verify(metadataValidator).validate(metadata);
    }

    @Test
    void shouldCallAssistantIdValidatorOnBuild() {
        builder.build();
        verify(idValidator).validateAssistantId(null);
    }

    @Test
    void shouldBuildWithAllParameters() {
        var metadata = Map.of("key", "value");
        var function = new Function("function_name", "description", Map.of("name", "value"));

        var actual = builder
                .assistantId("assistant-id")
                .instructions("instructions")
                .model(OpenAIModel.GPT_4_32K)
                .metadata(metadata)
                .codeInterpreter()
                .retrieval()
                .addFunction(function)
                .addThreadUserMessage("user message", Set.of(), metadata)
                .threadMetadata(metadata)
                .build();

        var expected = new CreateThreadAndRunRequest(
                "assistant-id",
                new CreateThreadRequestBuilder()
                        .addUserMessage("user message", Set.of(), metadata)
                        .metadata(metadata)
                        .build(),
                OpenAIModel.GPT_4_32K,
                "instructions",
                Set.of(
                        new Tool(ToolType.RETRIEVAL.getName(), null),
                        new Tool(ToolType.CODE_INTERPRETER.getName(), null),
                        new Tool(ToolType.FUNCTION_CALL.getName(), function)
                ),
                metadata
        );

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

}
