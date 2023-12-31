package br.com.rcaneppele.openai.common.json;

import br.com.rcaneppele.openai.error.APIError;
import br.com.rcaneppele.openai.error.APIErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter<T> {

    private final ObjectMapper mapper;
    private final Class<T> targetType;

    public JsonConverter(Class<T> targetType) {
        this.mapper = new ObjectMapperCreator().create();
        this.targetType = targetType;
    }

    public String convertRequestToJson(T request) {
        try {
            return this.mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error during serialization to json", e);
        }
    }

    public T convertJsonToResponse(String json) {
        try {
            return this.mapper.readValue(json, targetType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error during deserialization of json to response", e);
        }
    }

    public APIError convertJsonToApiError(String json) {
        try {
            return this.mapper.readValue(json, APIErrorResponse.class).getError();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error during deserialization of json to APIErrorResponse", e);
        }
    }

}
