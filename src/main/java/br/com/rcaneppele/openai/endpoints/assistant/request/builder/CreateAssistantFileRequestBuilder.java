package br.com.rcaneppele.openai.endpoints.assistant.request.builder;

import br.com.rcaneppele.openai.endpoints.assistant.request.CreateAssistantFileRequest;

public class CreateAssistantFileRequestBuilder {

    private String assistantId;
    private String fileId;

    public CreateAssistantFileRequestBuilder assistantId(String assistantId) {
        if (assistantId == null || assistantId.isBlank()) {
            throw new IllegalArgumentException("Assistant id is required!");
        }
        this.assistantId = assistantId;
        return this;
    }

    public CreateAssistantFileRequestBuilder fileId(String fileId) {
        if (fileId == null || fileId.isBlank()) {
            throw new IllegalArgumentException("File id is required!");
        }
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
        validateAssistantId();
        validateFileId();
    }

    private void validateAssistantId() {
        if (this.assistantId == null) {
            throw new IllegalArgumentException("Assistant id is required!");
        }
    }

    private void validateFileId() {
        if (this.fileId == null) {
            throw new IllegalArgumentException("File id is required!");
        }
    }

}
