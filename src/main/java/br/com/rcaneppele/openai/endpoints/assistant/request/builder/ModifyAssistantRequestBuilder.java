package br.com.rcaneppele.openai.endpoints.assistant.request.builder;

import br.com.rcaneppele.openai.endpoints.assistant.request.ModifyAssistantRequest;

public class ModifyAssistantRequestBuilder extends CreateAssistantRequestBuilder {

    private String assistantId;

    public ModifyAssistantRequestBuilder assistantId(String assistantId) {
        if (assistantId == null || assistantId.isBlank()) {
            throw new IllegalArgumentException("Assistant id is required!");
        }
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
        validateAssistantId();
    }

    private void validateAssistantId() {
        if (this.assistantId == null) {
            throw new IllegalArgumentException("Assistant id is required!");
        }
    }

}
