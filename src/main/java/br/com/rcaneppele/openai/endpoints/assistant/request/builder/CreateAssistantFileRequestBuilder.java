package br.com.rcaneppele.openai.endpoints.assistant.request.builder;

import br.com.rcaneppele.openai.common.validation.IdValidator;
import br.com.rcaneppele.openai.endpoints.assistant.request.CreateAssistantFileRequest;

public class CreateAssistantFileRequestBuilder {

    private String assistantId;
    private String fileId;

    private IdValidator idValidator = new IdValidator();

    public CreateAssistantFileRequestBuilder assistantId(String assistantId) {
        this.idValidator.validateAssistantId(assistantId);
        this.assistantId = assistantId;
        return this;
    }

    public CreateAssistantFileRequestBuilder fileId(String fileId) {
        this.idValidator.validateFileId(fileId);
        this.fileId = fileId;
        return this;
    }

    public CreateAssistantFileRequest build() {
        validateRequiredFields();

        return new CreateAssistantFileRequest(
                this.assistantId,
                this.fileId
        );
    }

    private void validateRequiredFields() {
        this.idValidator.validateAssistantId(this.assistantId);
        this.idValidator.validateFileId(this.fileId);
    }

}
