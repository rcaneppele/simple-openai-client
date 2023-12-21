package br.com.rcaneppele.openai.http;

import okhttp3.OkHttpClient;

import java.time.Duration;

public class HttpClientBuilder {

    public OkHttpClient build(Duration timeout) {
        return new OkHttpClient.Builder()
                .readTimeout(timeout)
                .addInterceptor(new DefaultContentTypeInterceptor())
                .addInterceptor(new ExceptionHandlerInterceptor())
                .build();
    }

}
