package br.com.rcaneppele.openai.endpoints.thread.request.builder;

import br.com.rcaneppele.openai.common.validation.IdValidator;
import br.com.rcaneppele.openai.common.validation.MetadataValidator;
import br.com.rcaneppele.openai.endpoints.thread.request.ModifyThreadRequest;

import java.util.Map;

public class ModifyThreadRequestBuilder {

    private String threadId;
    private Map<String, String> metadata;

    private MetadataValidator metadataValidator = new MetadataValidator();
    private IdValidator idValidator = new IdValidator();

    public ModifyThreadRequestBuilder threadId(String threadId) {
        this.idValidator.validateThreadId(threadId);
        this.threadId = threadId;
        return this;
    }

    public ModifyThreadRequestBuilder metadata(Map<String, String> metadata) {
        this.metadataValidator.validate(metadata);
        this.metadata = metadata;
        return this;
    }

    public ModifyThreadRequest build() {
        validateRequiredFields();

        return new ModifyThreadRequest(
                this.threadId,
                this.metadata
        );
    }

    private void validateRequiredFields() {
        this.idValidator.validateThreadId(this.threadId);
    }

}
