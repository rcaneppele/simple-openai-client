package br.com.rcaneppele.openai.endpoints.run.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum RunStepStatus {

    IN_PROGRESS,
    CANCELLED,
    FAILED,
    COMPLETED,
    EXPIRED;

    @JsonCreator
    public static RunStepStatus fromString(String value) {
        return Arrays.stream(RunStepStatus.values())
                .filter(status -> status.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown RunStepStatus enum constant: " + value));
    }

    @JsonValue
    public String toValue() {
        return name().toLowerCase();
    }

}
