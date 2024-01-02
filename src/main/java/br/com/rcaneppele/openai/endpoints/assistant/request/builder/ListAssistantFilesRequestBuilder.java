package br.com.rcaneppele.openai.endpoints.assistant.request.builder;

import br.com.rcaneppele.openai.endpoints.assistant.request.ListAssistantFilesRequest;

public class ListAssistantFilesRequestBuilder extends ListAssistantsRequestBuilder {

    private String assistantId;

    public ListAssistantFilesRequestBuilder assistantId(String assistantId) {
        if (assistantId == null || assistantId.isBlank()) {
            throw new IllegalArgumentException("Assistant id is required!");
        }
        this.assistantId = assistantId;
        return this;
    }

    public ListAssistantFilesRequest build() {
        validateRequiredFields();

        return new ListAssistantFilesRequest(
                this.assistantId,
                this.limit,
                this.order,
                this.after,
                this.before
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
