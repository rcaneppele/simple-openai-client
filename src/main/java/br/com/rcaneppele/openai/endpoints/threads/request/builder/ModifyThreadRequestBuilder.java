package br.com.rcaneppele.openai.endpoints.threads.request.builder;

import br.com.rcaneppele.openai.common.validation.MetadataValidator;
import br.com.rcaneppele.openai.endpoints.threads.request.ModifyThreadRequest;

import java.util.Map;

public class ModifyThreadRequestBuilder {

    private String threadId;
    private Map<String, String> metadata;
    private MetadataValidator metadataValidator = new MetadataValidator();

    public ModifyThreadRequestBuilder threadId(String threadId) {
        if (threadId == null || threadId.isBlank()) {
            throw new IllegalArgumentException("Thread id is required!");
        }
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
        validateThreadId();
    }

    private void validateThreadId() {
        if (this.threadId == null) {
            throw new IllegalArgumentException("Thread id is required!");
        }
    }

}
