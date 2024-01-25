package br.com.rcaneppele.openai.endpoints.assistant.request.builder;

import br.com.rcaneppele.openai.endpoints.assistant.request.ModifyAssistantRequest;

public class ModifyAssistantRequestBuilder extends CreateAssistantRequestBuilder {

    public ModifyAssistantRequest build() {
        return new ModifyAssistantRequest(
                this.model != null ? this.model : null,
                this.name,
                this.description,
                this.instructions,
                this.tools,
                this.fileIds,
                this.metadata
        );
    }

}
