package br.com.rcaneppele.openai.endpoints.run.request.builder;

import br.com.rcaneppele.openai.common.validation.MetadataValidator;
import br.com.rcaneppele.openai.endpoints.run.request.ModifyRunRequest;

import java.util.Map;

public class ModifyRunRequestBuilder {

    private Map<String, String> metadata;

    private MetadataValidator metadataValidator = new MetadataValidator();

    public ModifyRunRequestBuilder metadata(Map<String, String> metadata) {
        this.metadataValidator.validate(metadata);
        this.metadata = metadata;
        return this;
    }

    public ModifyRunRequest build() {
        return new ModifyRunRequest(
                this.metadata
        );
    }

}
