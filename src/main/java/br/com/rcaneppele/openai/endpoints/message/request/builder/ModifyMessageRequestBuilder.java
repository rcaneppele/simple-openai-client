package br.com.rcaneppele.openai.endpoints.message.request.builder;

import br.com.rcaneppele.openai.common.validation.MetadataValidator;
import br.com.rcaneppele.openai.endpoints.message.request.ModifyMessageRequest;

import java.util.Map;

public class ModifyMessageRequestBuilder {

    private Map<String, String> metadata;

    private MetadataValidator metadataValidator = new MetadataValidator();

    public ModifyMessageRequestBuilder metadata(Map<String, String> metadata) {
        this.metadataValidator.validate(metadata);
        this.metadata = metadata;
        return this;
    }

    public ModifyMessageRequest build() {
        return new ModifyMessageRequest(
                this.metadata
        );
    }

}
