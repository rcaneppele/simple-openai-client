package br.com.rcaneppele.openai.endpoints.run.request.builder;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.common.validation.IdValidator;
import br.com.rcaneppele.openai.common.validation.MetadataValidator;
import br.com.rcaneppele.openai.endpoints.assistant.tools.Function;
import br.com.rcaneppele.openai.endpoints.assistant.tools.Tool;
import br.com.rcaneppele.openai.endpoints.assistant.tools.ToolType;
import br.com.rcaneppele.openai.endpoints.run.request.CreateRunRequest;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CreateRunRequestBuilder {

    private String assistantId;
    private OpenAIModel model;
    private String instructions;
    private String additionalInstructions;
    private Set<Tool> tools = new HashSet<>();
    private Map<String, String> metadata;

    private IdValidator idValidator = new IdValidator();
    private MetadataValidator metadataValidator = new MetadataValidator();

    public CreateRunRequestBuilder assistantId(String assistantId) {
        this.validateAssistantId(assistantId);
        this.assistantId = assistantId;
        return this;
    }

    public CreateRunRequestBuilder model(OpenAIModel model) {
        if (model == null) {
            throw new IllegalArgumentException("Model cannot be null!");
        }
        this.model = model;
        return this;
    }

    public CreateRunRequestBuilder instructions(String instructions) {
        if (instructions != null && instructions.trim().length() > 32768) {
            throw new IllegalArgumentException("The maximum length of instructions is 32768 characters!");
        }
        this.instructions = instructions;
        return this;
    }

    public CreateRunRequestBuilder additionalInstructions(String additionalInstructions) {
        if (additionalInstructions != null && additionalInstructions.trim().length() > 32768) {
            throw new IllegalArgumentException("The maximum length of additional instructions is 32768 characters!");
        }
        this.additionalInstructions = additionalInstructions;
        return this;
    }

    public CreateRunRequestBuilder codeInterpreter() {
        this.tools.add(new Tool(ToolType.CODE_INTERPRETER.getName(), null));
        return this;
    }

    public CreateRunRequestBuilder retrieval() {
        this.tools.add(new Tool(ToolType.RETRIEVAL.getName(), null));
        return this;
    }

    public CreateRunRequestBuilder addFunction(Function function) {
        if (this.tools.size() == 128) {
            throw new IllegalArgumentException("There can be a maximum of 128 tools per run!");
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

    public CreateRunRequestBuilder metadata(Map<String, String> metadata) {
        this.metadataValidator.validate(metadata);
        this.metadata = metadata;
        return this;
    }

    public CreateRunRequest build() {
        validateRequiredFields();

        return new CreateRunRequest(
                this.assistantId,
                this.model,
                this.instructions,
                this.additionalInstructions,
                this.tools,
                this.metadata
        );
    }

    private void validateRequiredFields() {
        validateAssistantId(this.assistantId);
    }

    private void validateAssistantId(String assistantId) {
        this.idValidator.validateAssistantId(assistantId);
    }

}
