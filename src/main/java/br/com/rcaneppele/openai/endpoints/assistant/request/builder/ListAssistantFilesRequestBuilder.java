package br.com.rcaneppele.openai.endpoints.assistant.request.builder;

import br.com.rcaneppele.openai.common.validation.IdValidator;
import br.com.rcaneppele.openai.endpoints.assistant.request.ListAssistantFilesRequest;

public class ListAssistantFilesRequestBuilder extends ListAssistantsRequestBuilder {

    private String assistantId;

    private IdValidator idValidator = new IdValidator();;

    public ListAssistantFilesRequestBuilder assistantId(String assistantId) {
        this.idValidator.validateAssistantId(assistantId);
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
        this.idValidator.validateAssistantId(this.assistantId);
    }

}
