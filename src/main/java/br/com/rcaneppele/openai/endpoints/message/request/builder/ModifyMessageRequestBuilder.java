package br.com.rcaneppele.openai.endpoints.message.request.builder;

import br.com.rcaneppele.openai.common.validation.IdValidator;
import br.com.rcaneppele.openai.common.validation.MetadataValidator;
import br.com.rcaneppele.openai.endpoints.message.request.ModifyMessageRequest;

import java.util.Map;

public class ModifyMessageRequestBuilder {

    private String threadId;
    private String messageId;
    private Map<String, String> metadata;

    private IdValidator idValidator = new IdValidator();
    private MetadataValidator metadataValidator = new MetadataValidator();

    public ModifyMessageRequestBuilder threadId(String threadId) {
        this.validateThreadId(threadId);
        this.threadId = threadId;
        return this;
    }

    public ModifyMessageRequestBuilder messageId(String messageId) {
        this.validateMessageId(messageId);
        this.messageId = messageId;
        return this;
    }

    public ModifyMessageRequestBuilder metadata(Map<String, String> metadata) {
        this.metadataValidator.validate(metadata);
        this.metadata = metadata;
        return this;
    }

    public ModifyMessageRequest build() {
        validateRequiredFields();

        return new ModifyMessageRequest(
                this.threadId,
                this.messageId,
                this.metadata
        );
    }

    private void validateRequiredFields() {
        validateThreadId(this.threadId);
        validateMessageId(this.messageId);
    }

    private void validateThreadId(String threadId) {
        this.idValidator.validateThreadId(threadId);
    }

    private void validateMessageId(String messageId) {
        this.idValidator.validateMessageId(messageId);
    }

}
