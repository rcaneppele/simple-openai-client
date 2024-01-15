package br.com.rcaneppele.openai.endpoints.run.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum RunStepType {

    MESSAGE_CREATION,
    TOOL_CALLS;

    @JsonCreator
    public static RunStepType fromString(String value) {
        return Arrays.stream(RunStepType.values())
                .filter(status -> status.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown RunStepType enum constant: " + value));
    }

    @JsonValue
    public String toValue() {
        return name().toLowerCase();
    }

}
