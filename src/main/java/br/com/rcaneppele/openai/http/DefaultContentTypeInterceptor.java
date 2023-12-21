package br.com.rcaneppele.openai.http;

import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;

class DefaultContentTypeInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        var newRequest = chain.request().newBuilder()
                .header("Content-Type", "application/json; charset=utf-8")
                .build();

        return chain.proceed(newRequest);
    }

}
