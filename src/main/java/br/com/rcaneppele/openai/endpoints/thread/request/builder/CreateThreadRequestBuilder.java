package br.com.rcaneppele.openai.endpoints.thread.request.builder;

import br.com.rcaneppele.openai.common.validation.MetadataValidator;
import br.com.rcaneppele.openai.endpoints.message.request.CreateMessageRequest;
import br.com.rcaneppele.openai.endpoints.thread.request.CreateThreadRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CreateThreadRequestBuilder {

    private List<CreateMessageRequest> messages = new ArrayList<>();
    private Map<String, String> metadata;

    private MetadataValidator metadataValidator = new MetadataValidator();

    public CreateThreadRequestBuilder addUserMessage(String content, Set<String> fileIds, Map<String, String> metadata) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("The content of the message is required!");
        }

        if (fileIds != null && fileIds.size() > 10) {
            throw new IllegalArgumentException("There can be a maximum of 10 files attached to a message!");
        }

        if (metadata != null) {
            this.metadataValidator.validate(metadata);
        }

        var message = new CreateMessageRequest(
                null,
                "user",
                content,
                fileIds,
                metadata
        );
        this.messages.add(message);
        return this;
    }

    public CreateThreadRequestBuilder metadata(Map<String, String> metadata) {
        this.metadataValidator.validate(metadata);
        this.metadata = metadata;
        return this;
    }

    public CreateThreadRequest build() {
        return new CreateThreadRequest(
                this.messages,
                this.metadata
        );
    }

}
