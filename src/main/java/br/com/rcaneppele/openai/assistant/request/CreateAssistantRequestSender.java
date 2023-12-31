package br.com.rcaneppele.openai.assistant.request;

import br.com.rcaneppele.openai.assistant.response.CreateAssistantResponse;
import br.com.rcaneppele.openai.assistant.response.CreateAssistantResponseBuilder;
import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.http.HttpClientBuilder;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.time.Duration;

public class CreateAssistantRequestSender {

    private static final String CHAT_COMPLETION_URI = "assistants";
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json");

    private final OkHttpClient http;
    private final String apiKey;
    private final String createAssistantUrl;
    private final JsonConverter<CreateAssistantRequest> jsonConverter;

    public CreateAssistantRequestSender(String apiBaseUrl, Duration timeout, String apiKey) {
        this.createAssistantUrl = apiBaseUrl + CHAT_COMPLETION_URI;
        this.http = new HttpClientBuilder().build(timeout);
        this.apiKey = apiKey;
        this.jsonConverter = new JsonConverter(CreateAssistantRequest.class);
    }

    public CreateAssistantResponse sendRequest(CreateAssistantRequest request) {
        var json = jsonConverter.convertRequestToJson(request);
        var httpRequest =  new Request.Builder()
                .url(createAssistantUrl)
                .header("Authorization", "Bearer " +this.apiKey)
                .header("OpenAI-Beta", "assistants=v1")
                .post(RequestBody.create(json, MEDIA_TYPE))
                .build();

        try {
            var response = http.newCall(httpRequest).execute();
            return new CreateAssistantResponseBuilder().build(response);
        } catch (IOException e) {
            throw new RuntimeException("Error sending create assistant request", e);
        }
    }

}
