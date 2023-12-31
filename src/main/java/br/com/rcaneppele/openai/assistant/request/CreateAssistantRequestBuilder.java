package br.com.rcaneppele.openai.assistant.request;

import br.com.rcaneppele.openai.assistant.tools.Function;
import br.com.rcaneppele.openai.assistant.tools.Tool;
import br.com.rcaneppele.openai.assistant.tools.ToolType;
import br.com.rcaneppele.openai.common.OpenAIModel;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CreateAssistantRequestBuilder {

    private OpenAIModel model;
    private String name;
    private String description;
    private String instructions;
    private Set<Tool> tools = new HashSet<>();
    private Set<String> fileIds = new HashSet<>();
    private Map<String, String> metadata;

    public CreateAssistantRequestBuilder model(OpenAIModel model) {
        if (model == null) {
            throw new IllegalArgumentException("Model cannot be null!");
        }
        this.model = model;
        return this;
    }

    public CreateAssistantRequestBuilder name(String name) {
        if (name != null && name.trim().length() > 256) {
            throw new IllegalArgumentException("The maximum length of name is 256 characters!");
        }
        this.name = name;
        return this;
    }

    public CreateAssistantRequestBuilder description(String description) {
        if (description != null && description.trim().length() > 512) {
            throw new IllegalArgumentException("The maximum length of description is 512 characters!");
        }
        this.description = description;
        return this;
    }

    public CreateAssistantRequestBuilder instructions(String instructions) {
        if (instructions != null && instructions.trim().length() > 32768) {
            throw new IllegalArgumentException("The maximum length of instructions is 32768 characters!");
        }
        this.instructions = instructions;
        return this;
    }

    public CreateAssistantRequestBuilder codeInterpreter() {
        this.tools.add(new Tool(ToolType.CODE_INTERPRETER.getName(), null));
        return this;
    }

    public CreateAssistantRequestBuilder retrieval() {
        this.tools.add(new Tool(ToolType.RETRIEVAL.getName(), null));
        return this;
    }

    public CreateAssistantRequestBuilder addFunction(Function function) {
        if (this.tools.size() == 128) {
            throw new IllegalArgumentException("There can be a maximum of 128 tools per assistant!");
        }

        if (function == null) {
            throw new IllegalArgumentException("Function is required!");
        }

        var name = function.name();
        var regex = "^[a-zA-Z0-9_-]{1,64}$";
        if (name == null || name.isBlank() || !name.matches(regex)) {
            throw new IllegalArgumentException("Function name is required and must be a-z, A-Z, 0-9, or contain underscores and dashes, with a maximum length of 64!");
        }

        this.tools.add(new Tool(ToolType.FUNCTION_CALL.getName(), function));
        return this;
    }

    public CreateAssistantRequestBuilder fileIds(String... ids) {
        if (ids.length > 20) {
            throw new IllegalArgumentException("There can be a maximum of 20 files attached to the assistant!");
        }
        this.fileIds.addAll(List.of(ids));
        return this;
    }

    public CreateAssistantRequestBuilder metadata(Map<String, String> metadata) {
        if (metadata.size() > 16) {
            throw new IllegalArgumentException("There can be a maximum of 16 key-value pairs that can be attached to the assistant!");
        }

        var keysValid = metadata.keySet().stream().allMatch(key -> key.length() <= 64);
        if (!keysValid) {
            throw new IllegalArgumentException("Metadata Keys can be a maximum of 64 characters long!");
        }

        var valuesValid = metadata.values().stream().allMatch(key -> key.length() <= 512);
        if (!valuesValid) {
            throw new IllegalArgumentException("Metadata Values can be a maximum of 512 characters long!");
        }

        this.metadata = metadata;
        return this;
    }

    public CreateAssistantRequest build() {
        validateRequiredFields();

        return new CreateAssistantRequest(
                this.model.getName(),
                this.name,
                this.description,
                this.instructions,
                this.tools,
                this.fileIds,
                this.metadata
        );
    }

    private void validateRequiredFields() {
        validateModel();
    }

    private void validateModel() {
        if (this.model == null) {
            throw new IllegalArgumentException("Model is required!");
        }
    }

}
