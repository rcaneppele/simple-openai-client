package br.com.rcaneppele.openai.endpoints.thread.request.builder;

import br.com.rcaneppele.openai.common.validation.MetadataValidator;
import br.com.rcaneppele.openai.endpoints.thread.request.ModifyThreadRequest;

import java.util.Map;

public class ModifyThreadRequestBuilder {

    private Map<String, String> metadata;

    private MetadataValidator metadataValidator = new MetadataValidator();

    public ModifyThreadRequestBuilder metadata(Map<String, String> metadata) {
        this.metadataValidator.validate(metadata);
        this.metadata = metadata;
        return this;
    }

    public ModifyThreadRequest build() {
        return new ModifyThreadRequest(
                this.metadata
        );
    }

}
