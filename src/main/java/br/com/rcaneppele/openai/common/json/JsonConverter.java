package br.com.rcaneppele.openai.common.json;

import br.com.rcaneppele.openai.chatcompletion.request.ChatCompletionRequest;
import br.com.rcaneppele.openai.chatcompletion.response.ChatCompletionResponse;
import br.com.rcaneppele.openai.error.APIError;
import br.com.rcaneppele.openai.error.APIErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter {

    private final ObjectMapper mapper;

    public JsonConverter() {
        this.mapper = new ObjectMapperCreator().create();
    }

    public String convertChatCompletionRequestToJson(ChatCompletionRequest request) {
        try {
            return this.mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error during serialization of ChatCompletionRequest to json", e);
        }
    }

    public ChatCompletionResponse convertJsonToChatCompletionResponse(String json) {
        try {
            return this.mapper.readValue(json, ChatCompletionResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error during deserialization of json to ChatCompletionResponse", e);
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
