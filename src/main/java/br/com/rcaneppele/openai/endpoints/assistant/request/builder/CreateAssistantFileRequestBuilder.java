package br.com.rcaneppele.openai.endpoints.assistant.request.builder;

import br.com.rcaneppele.openai.common.validation.IdValidator;
import br.com.rcaneppele.openai.endpoints.assistant.request.CreateAssistantFileRequest;

public class CreateAssistantFileRequestBuilder {

    private String fileId;

    private IdValidator idValidator = new IdValidator();

    public CreateAssistantFileRequestBuilder fileId(String fileId) {
        this.idValidator.validateFileId(fileId);
        this.fileId = fileId;
        return this;
    }

    public CreateAssistantFileRequest build() {
        validateRequiredFields();

        return new CreateAssistantFileRequest(
                this.fileId
        );
    }

    private void validateRequiredFields() {
        this.idValidator.validateFileId(this.fileId);
    }

}
