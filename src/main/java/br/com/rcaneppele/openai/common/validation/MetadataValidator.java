package br.com.rcaneppele.openai.common.validation;

import java.util.Map;

public class MetadataValidator {

    public void validate(Map<String, String> metadata) {
        if (metadata.size() > 16) {
            throw new IllegalArgumentException("There can be a maximum of 16 key-value pairs that can be attached to the assistant!");
        }

        var keysValid = metadata.keySet().stream().allMatch(key -> key.length() <= 64);
        if (!keysValid) {
            throw new IllegalArgumentException("Metadata Keys can be a maximum of 64 characters long!");
        }

        var valuesValid = metadata.values().stream().allMatch(key -> key.length() <= 512);
        if (!valuesValid) {
            throw new IllegalArgumentException("Metadata Values can be a maximum of 512 characters long!");
        }
    }

}
