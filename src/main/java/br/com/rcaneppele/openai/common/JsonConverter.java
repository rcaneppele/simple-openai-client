package br.com.rcaneppele.openai.common;

import br.com.rcaneppele.openai.chatcompletion.request.ChatCompletionRequest;
import br.com.rcaneppele.openai.chatcompletion.response.ChatCompletionResponse;
import br.com.rcaneppele.openai.error.APIError;
import br.com.rcaneppele.openai.error.APIErrorResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter {

    private final ObjectMapper mapper;

    public JsonConverter() {
        this.mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public String convertChatCompletionRequestToJson(ChatCompletionRequest request) {
        try {
            return mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error during serialization of ChatCompletionRequest to json", e);
        }
    }

    public ChatCompletionResponse convertJsonToChatCompletionResponse(String json) {
        try {
            return mapper.readValue(json, ChatCompletionResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error during deserialization of json to ChatCompletionResponse", e);
        }
    }

    public APIError convertJsonToApiError(String json) {
        try {
            return mapper.readValue(json, APIErrorResponse.class).getError();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error during deserialization of json to APIErrorResponse", e);
        }
    }

}
