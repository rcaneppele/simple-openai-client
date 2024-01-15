package br.com.rcaneppele.openai.endpoints.run.request.builder;

import br.com.rcaneppele.openai.common.validation.IdValidator;
import br.com.rcaneppele.openai.common.validation.MetadataValidator;
import br.com.rcaneppele.openai.endpoints.run.request.ModifyRunRequest;

import java.util.Map;

public class ModifyRunRequestBuilder {

    private String threadId;
    private String runId;
    private Map<String, String> metadata;

    private IdValidator idValidator = new IdValidator();
    private MetadataValidator metadataValidator = new MetadataValidator();

    public ModifyRunRequestBuilder threadId(String threadId) {
        this.validateThreadId(threadId);
        this.threadId = threadId;
        return this;
    }

    public ModifyRunRequestBuilder runId(String runId) {
        this.validateRunId(runId);
        this.runId = runId;
        return this;
    }

    public ModifyRunRequestBuilder metadata(Map<String, String> metadata) {
        this.metadataValidator.validate(metadata);
        this.metadata = metadata;
        return this;
    }

    public ModifyRunRequest build() {
        validateRequiredFields();

        return new ModifyRunRequest(
                this.threadId,
                this.runId,
                this.metadata
        );
    }

    private void validateRequiredFields() {
        validateThreadId(this.threadId);
        validateRunId(this.runId);
    }

    private void validateThreadId(String threadId) {
        this.idValidator.validateThreadId(threadId);
    }

    private void validateRunId(String runId) {
        this.idValidator.validateRunId(runId);
    }

}
