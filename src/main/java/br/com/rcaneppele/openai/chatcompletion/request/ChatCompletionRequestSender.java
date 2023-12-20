package br.com.rcaneppele.openai.chatcompletion.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.time.Duration;

public final class ChatCompletionRequestSender {

    private static final String CHAT_COMPLETION_URI = "chat/completions";
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient http;
    private final String apiKey;
    private final String chatCompletionUrl;

    public ChatCompletionRequestSender(String apiBaseUrl, Duration timeout, String apiKey) {
        this.chatCompletionUrl = apiBaseUrl + CHAT_COMPLETION_URI;
        this.http = new OkHttpClient.Builder().readTimeout(timeout).build();
        this.apiKey = apiKey;
    }

    public String sendRequest(ChatCompletionRequest request) {
        var httpRequest =  new Request.Builder()
                .url(chatCompletionUrl)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " +this.apiKey)
                .post(RequestBody.create(generateJsonForRequest(request), MEDIA_TYPE))
                .build();

        try {
            var response = http.newCall(httpRequest).execute();
            return response.body().string();
        } catch (Exception e) {
            throw new RuntimeException("Error sending chat completion request", e);
        }
    }

    private String generateJsonForRequest(ChatCompletionRequest request) {
        try {
            var objectMapper = createObjectMapper();
            return objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error during serialization of request to json", e);
        }
    }

    private ObjectMapper createObjectMapper() {
        var mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

}
