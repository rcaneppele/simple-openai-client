package br.com.rcaneppele.openai.chatcompletion.request.stream;

import br.com.rcaneppele.openai.chatcompletion.request.ChatCompletionRequest;
import br.com.rcaneppele.openai.chatcompletion.request.ChatCompletionRequestSender;
import br.com.rcaneppele.openai.chatcompletion.response.ChatCompletionResponse;
import br.com.rcaneppele.openai.common.json.JsonConverter;
import br.com.rcaneppele.openai.error.APIErrorHandler;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.Duration;

public class ChatCompletionStreamRequestSender extends ChatCompletionRequestSender {

    public ChatCompletionStreamRequestSender(String apiBaseUrl, Duration timeout, String apiKey) {
        super(apiBaseUrl, timeout, apiKey);
    }

    public Observable<ChatCompletionResponse> sendStreamRequest(ChatCompletionRequest request) {
        return Observable.create(emitter -> {
            var json = jsonConverter.convertRequestToJson(request);
            var httpRequest = new Request.Builder()
                    .url(requestUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Accept", "text/event-stream")
                    .post(RequestBody.create(json, MediaType.parse("application/json")))
                    .build();

            try (var response = httpClient.newCall(httpRequest).execute()) {
                streamResponse(emitter, response);
            } catch (Exception e) {
                if (!emitter.isDisposed()) {
                    emitter.onError(e);
                }
            }
        });
    }

    private void streamResponse(ObservableEmitter<ChatCompletionResponse> emitter, Response response) throws IOException {
        if (!response.isSuccessful()) {
            new APIErrorHandler().handleError(response);
        }

        var streamJsonConvert = new JsonConverter<ChatCompletionResponse>(ChatCompletionResponse.class);

        var reader = new BufferedReader(response.body().charStream());
        String line;
        while ((line = reader.readLine()) != null && !emitter.isDisposed()) {
            if (line.equals("data: [DONE]")) {
                emitter.onComplete();
                break;
            }

            if (line.startsWith("data: ")) {
                var json = line.substring(6).trim();
                if (!json.isEmpty()) {
                    var partialResponse = streamJsonConvert.convertJsonToResponse(json);
                    emitter.onNext(partialResponse);
                }
            }
        }
    }

}
