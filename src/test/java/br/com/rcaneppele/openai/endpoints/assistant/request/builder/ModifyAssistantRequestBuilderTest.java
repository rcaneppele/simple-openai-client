package br.com.rcaneppele.openai.endpoints.assistant.request.builder;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.common.validation.MetadataValidator;
import br.com.rcaneppele.openai.endpoints.assistant.request.ModifyAssistantRequest;
import br.com.rcaneppele.openai.endpoints.assistant.tools.Function;
import br.com.rcaneppele.openai.endpoints.assistant.tools.Tool;
import br.com.rcaneppele.openai.endpoints.assistant.tools.ToolType;
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
class ModifyAssistantRequestBuilderTest {

    @InjectMocks
    private ModifyAssistantRequestBuilder builder;

    @Mock
    private MetadataValidator metadataValidator;

    @Test
    void shouldNotAddNullFunction() {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.addFunction(null));
        assertEquals("Function is required!", exception.getMessage());
    }

    @Test
    void shouldNotAddFunctionWithNullName() {
        var function = new Function(null, null, null);
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.addFunction(function));
        assertEquals("Function name is required and must be a-z, A-Z, 0-9, or contain underscores and dashes, with a maximum length of 64!", exception.getMessage());
    }

    @ParameterizedTest(name = "Testing function name with: {0}")
    @ValueSource(strings = {"@", "$", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
    void shouldNotAddFunctionWithInvalidName(String name) {
        var function = new Function(name, null, null);
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.addFunction(function));
        assertEquals("Function name is required and must be a-z, A-Z, 0-9, or contain underscores and dashes, with a maximum length of 64!", exception.getMessage());
    }

    @ParameterizedTest(name = "Testing function name with: {0}")
    @ValueSource(strings = {"x", "123", "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"})
    void shouldAddFunctionWithValidName(String name) {
        var function = new Function(name, null, null);
        assertDoesNotThrow(() -> builder.addFunction(function));
    }

    @Test
    void shouldNotAddMoreThan128Tools() {
        for (var i = 1; i <= 128; i++) {
            var name = "function" + i;
            var function = new Function(name, null, null);
            assertDoesNotThrow(() -> builder.addFunction(function));
        }

        var name = "function " + 129;
        var function = new Function(name, null, null);
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.addFunction(function));
        assertEquals("There can be a maximum of 128 tools per assistant!", exception.getMessage());
    }

    @Test
    public void shouldAddRetrieval() {
        builder.retrieval();
        var request = builder.build();
        assertEquals(1, request.tools().size());
        var retrieval = new Tool(ToolType.RETRIEVAL.getName(), null);
        assertTrue(request.tools().contains(retrieval));
    }

    @Test
    public void shouldAddCodeInterpreter() {
        builder.codeInterpreter();
        var request = builder.build();
        assertEquals(1, request.tools().size());
        var retrieval = new Tool(ToolType.CODE_INTERPRETER.getName(), null);
        assertTrue(request.tools().contains(retrieval));
    }

    @Test
    void shouldNotAddMoreThan20FileIds() {
        var ids = new String[21];
        for (int i = 0; i <= 20; i++) {
            ids[i] = String.valueOf(i);
        }

        var exception = assertThrows(IllegalArgumentException.class, () -> builder.fileIds(ids));
        assertEquals("There can be a maximum of 20 files attached to the assistant!", exception.getMessage());
    }

    @Test
    void shouldCallMetadataValidator() {
        var metadata = Map.of("key", "value");
        builder.metadata(metadata);
        verify(metadataValidator).validate(metadata);
    }

    @Test
    void shouldBuildWithAllParameters() {
        var name = "The name";
        var description = "The description";
        var instructions = "The instructions";
        var function = new Function("Function-name", "Function description", Map.of("key", "value"));
        var fileIds = new String[]{"file-1", "file-2"};
        var metadata = Map.of("key1", "value-2", "key-2", "value-2");

        var request = (ModifyAssistantRequest) builder
                .model(OpenAIModel.GPT_3_5_TURBO)
                .name(name)
                .description(description)
                .instructions(instructions)
                .retrieval()
                .codeInterpreter()
                .addFunction(function)
                .fileIds(fileIds)
                .metadata(metadata)
                .build();

        assertEquals(OpenAIModel.GPT_3_5_TURBO, request.model());
        assertEquals(name, request.name());
        assertEquals(description, request.description());
        assertEquals(instructions, request.instructions());
        assertEquals(Set.of(fileIds), request.fileIds());
        assertEquals(metadata, request.metadata());
        assertEquals(3, request.tools().size());
        assertTrue(request.tools().containsAll(Set.of(
                new Tool(ToolType.RETRIEVAL.getName(), null),
                new Tool(ToolType.CODE_INTERPRETER.getName(), null),
                new Tool(ToolType.FUNCTION_CALL.getName(), function)
        )));
    }

}
