package br.com.rcaneppele.openai.common.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum ErrorType {

    SERVER_ERROR,
    RATE_LIMIT_EXCEEDED;

    @JsonCreator
    public static ErrorType fromString(String value) {
        return Arrays.stream(ErrorType.values())
                .filter(errorType -> errorType.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown ErrorType enum constant: " + value));
    }

    @JsonValue
    public String toValue() {
        return name().toLowerCase();
    }
}
