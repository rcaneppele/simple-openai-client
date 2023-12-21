package br.com.rcaneppele.openai.http;

import br.com.rcaneppele.openai.error.exception.TimeoutException;
import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;
import java.net.SocketTimeoutException;

class ExceptionHandlerInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        try {
            return chain.proceed(chain.request());
        } catch (SocketTimeoutException ste) {
            throw new TimeoutException("Connection timeout! Increase timeout on OpenAIClient object.");
        }
    }

}
