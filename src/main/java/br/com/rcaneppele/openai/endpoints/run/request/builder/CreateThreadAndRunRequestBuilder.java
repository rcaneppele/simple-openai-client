package br.com.rcaneppele.openai.endpoints.run.request.builder;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.common.validation.IdValidator;
import br.com.rcaneppele.openai.common.validation.MetadataValidator;
import br.com.rcaneppele.openai.endpoints.assistant.tools.Function;
import br.com.rcaneppele.openai.endpoints.assistant.tools.Tool;
import br.com.rcaneppele.openai.endpoints.assistant.tools.ToolType;
import br.com.rcaneppele.openai.endpoints.run.request.CreateThreadAndRunRequest;
import br.com.rcaneppele.openai.endpoints.thread.request.builder.CreateThreadRequestBuilder;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CreateThreadAndRunRequestBuilder {

    private String assistantId;
    private OpenAIModel model;
    private String instructions;
    private Set<Tool> tools = new HashSet<>();
    private Map<String, String> metadata;

    private IdValidator idValidator = new IdValidator();
    private MetadataValidator metadataValidator = new MetadataValidator();

    private CreateThreadRequestBuilder threadRequestBuilder = new CreateThreadRequestBuilder();

    public CreateThreadAndRunRequestBuilder assistantId(String assistantId) {
        this.validateAssistantId(assistantId);
        this.assistantId = assistantId;
        return this;
    }

    public CreateThreadAndRunRequestBuilder model(OpenAIModel model) {
        if (model == null) {
            throw new IllegalArgumentException("Model cannot be null!");
        }
        this.model = model;
        return this;
    }

    public CreateThreadAndRunRequestBuilder instructions(String instructions) {
        if (instructions != null && instructions.trim().length() > 32768) {
            throw new IllegalArgumentException("The maximum length of instructions is 32768 characters!");
        }
        this.instructions = instructions;
        return this;
    }

    public CreateThreadAndRunRequestBuilder codeInterpreter() {
        this.tools.add(new Tool(ToolType.CODE_INTERPRETER.getName(), null));
        return this;
    }

    public CreateThreadAndRunRequestBuilder retrieval() {
        this.tools.add(new Tool(ToolType.RETRIEVAL.getName(), null));
        return this;
    }

    public CreateThreadAndRunRequestBuilder addFunction(Function function) {
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

    public CreateThreadAndRunRequestBuilder metadata(Map<String, String> metadata) {
        this.metadataValidator.validate(metadata);
        this.metadata = metadata;
        return this;
    }

    public CreateThreadAndRunRequestBuilder addThreadUserMessage(String content, Set<String> fileIds, Map<String, String> metadata) {
        this.threadRequestBuilder.addUserMessage(content, fileIds, metadata);
        return this;
    }

    public CreateThreadAndRunRequestBuilder threadMetadata(Map<String, String> metadata) {
        this.threadRequestBuilder.metadata(metadata);
        return this;
    }

    public CreateThreadAndRunRequest build() {
        validateRequiredFields();

        return new CreateThreadAndRunRequest(
                this.assistantId,
                this.threadRequestBuilder.build(),
                this.model,
                this.instructions,
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
