package br.com.rcaneppele.openai.endpoints.run.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum LastErrorCode {

    SERVER_ERROR,
    RATE_LIMIT_EXCEEDED;

    @JsonCreator
    public static LastErrorCode fromString(String value) {
        return Arrays.stream(LastErrorCode.values())
                .filter(status -> status.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown LastErrorCode enum constant: " + value));
    }

    @JsonValue
    public String toValue() {
        return name().toLowerCase();
    }

}
