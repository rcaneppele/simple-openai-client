package br.com.rcaneppele.openai.endpoints.message.request.builder;

import br.com.rcaneppele.openai.common.validation.IdValidator;
import br.com.rcaneppele.openai.common.validation.MetadataValidator;
import br.com.rcaneppele.openai.endpoints.message.request.CreateMessageRequest;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CreateMessageRequestBuilder {

    private String threadId;
    private String role = "user"; // Currently only user is supported
    private String content;
    private Set<String> fileIds = new HashSet<>();
    private Map<String, String> metadata;

    private IdValidator idValidator = new IdValidator();
    private MetadataValidator metadataValidator = new MetadataValidator();

    public CreateMessageRequestBuilder threadId(String threadId) {
        this.validateThreadId(threadId);
        this.threadId = threadId;
        return this;
    }

    public CreateMessageRequestBuilder content(String content) {
        this.validateContent(content);
        this.content = content;
        return this;
    }

    public CreateMessageRequestBuilder fileIds(String... ids) {
        if (ids.length > 10) {
            throw new IllegalArgumentException("There can be a maximum of 10 files attached to a message!");
        }
        this.fileIds.addAll(Set.of(ids));
        return this;
    }

    public CreateMessageRequestBuilder metadata(Map<String, String> metadata) {
        this.metadataValidator.validate(metadata);
        this.metadata = metadata;
        return this;
    }

    public CreateMessageRequest build() {
        validateRequiredFields();

        return new CreateMessageRequest(
                this.threadId,
                this.role,
                this.content,
                this.fileIds,
                this.metadata
        );
    }

    private void validateRequiredFields() {
        validateThreadId(this.threadId);
        validateContent(this.content);
    }

    private void validateThreadId(String threadId) {
        this.idValidator.validateThreadId(threadId);
    }

    private void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("The content of the message is required!");
        }
    }

}
