package br.com.rcaneppele.openai.endpoints.assistant.request.builder;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.endpoints.assistant.request.ModifyAssistantRequest;
import br.com.rcaneppele.openai.endpoints.assistant.tools.Function;
import br.com.rcaneppele.openai.endpoints.assistant.tools.Tool;
import br.com.rcaneppele.openai.endpoints.assistant.tools.ToolType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ModifyAssistantRequestBuilderTest {

    private ModifyAssistantRequestBuilder builder;

    @BeforeEach
    void beforeEach() {
        this. builder = new ModifyAssistantRequestBuilder();
    }

    @Test
    void shouldValidateAssistantId() {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.assistantId(null));
        assertEquals("Assistant id is required!", exception.getMessage());
    }

    @Test
    void shouldNotBuildWithoutAssistantId() {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.build());
        assertEquals("Assistant id is required!", exception.getMessage());
    }

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
        var request = builder.assistantId("asst_123").build();
        assertEquals(1, request.tools().size());
        var retrieval = new Tool(ToolType.RETRIEVAL.getName(), null);
        assertTrue(request.tools().contains(retrieval));
    }

    @Test
    public void shouldAddCodeInterpreter() {
        builder.codeInterpreter();
        var request = builder.assistantId("asst_123").build();
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
    void shouldNotAddMoreThan16Metadata() {
        var metadata = new HashMap<String, String>();
        for (int i = 1; i <= 17; i++) {
            var actual = String.valueOf(i);
            metadata.put(actual, actual);
        }

        var exception = assertThrows(IllegalArgumentException.class, () -> builder.metadata(metadata));
        assertEquals("There can be a maximum of 16 key-value pairs that can be attached to the assistant!", exception.getMessage());
    }

    @Test
    void shouldNotAddMetadataWithInvalidKey() {
        var invalidKey = "a".repeat(65);
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.metadata(Map.of(invalidKey, "value")));
        assertEquals("Metadata Keys can be a maximum of 64 characters long!", exception.getMessage());
    }

    @Test
    void shouldNotAddMetadataWithInvalidValue() {
        var invalidValue = "a".repeat(513);
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.metadata(Map.of("key", invalidValue)));
        assertEquals("Metadata Values can be a maximum of 512 characters long!", exception.getMessage());
    }

    @Test
    void shouldBuildWithAllParameters() {
        var assistantId = "asst_123";
        var name = "The name";
        var description = "The description";
        var instructions = "The instructions";
        var function = new Function("Function-name", "Function description", Map.of("key", "value"));
        var fileIds = new String[]{"file-1", "file-2"};
        var metadata = Map.of("key1", "value-2", "key-2", "value-2");

        var request = (ModifyAssistantRequest) builder
                .assistantId(assistantId)
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

        assertEquals(assistantId, request.assistantId());
        assertEquals(OpenAIModel.GPT_3_5_TURBO.getName(), request.model());
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
