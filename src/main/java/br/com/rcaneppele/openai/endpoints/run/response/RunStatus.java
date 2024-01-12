package br.com.rcaneppele.openai.endpoints.run.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum RunStatus {

    QUEUED,
    IN_PROGRESS,
    REQUIRES_ACTION,
    CANCELLING,
    CANCELLED,
    FAILED,
    COMPLETED,
    EXPIRED;

    @JsonCreator
    public static RunStatus fromString(String value) {
        return Arrays.stream(RunStatus.values())
                .filter(status -> status.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown RunStatus enum constant: " + value));
    }

    @JsonValue
    public String toValue() {
        return name().toLowerCase();
    }

}
