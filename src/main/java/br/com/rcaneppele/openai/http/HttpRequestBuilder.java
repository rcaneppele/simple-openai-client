package br.com.rcaneppele.openai.http;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpRequestBuilder {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/";
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    public Request buildPost(String uri, String apiKey, String json) {
        return new Request.Builder()
                .url(OPENAI_API_URL + uri)
                .header("Authorization", "Bearer " +apiKey)
                .post(RequestBody.create(json, MEDIA_TYPE))
                .build();
    }

}
