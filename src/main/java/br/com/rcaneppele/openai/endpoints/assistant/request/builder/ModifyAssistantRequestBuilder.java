package br.com.rcaneppele.openai.endpoints.assistant.request.builder;

import br.com.rcaneppele.openai.common.validation.IdValidator;
import br.com.rcaneppele.openai.endpoints.assistant.request.ModifyAssistantRequest;

public class ModifyAssistantRequestBuilder extends CreateAssistantRequestBuilder {

    private String assistantId;

    private IdValidator idValidator = new IdValidator();

    public ModifyAssistantRequestBuilder assistantId(String assistantId) {
        this.idValidator.validateAssistantId(assistantId);
        this.assistantId = assistantId;
        return this;
    }

    public ModifyAssistantRequest build() {
        validateRequiredFields();

        return new ModifyAssistantRequest(
                this.assistantId,
                this.model != null ? this.model.getName() : null,
                this.name,
                this.description,
                this.instructions,
                this.tools,
                this.fileIds,
                this.metadata
        );
    }

    private void validateRequiredFields() {
        this.idValidator.validateAssistantId(this.assistantId);
    }

}
