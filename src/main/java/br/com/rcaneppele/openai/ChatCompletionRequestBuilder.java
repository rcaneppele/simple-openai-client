package br.com.rcaneppele.openai;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ChatCompletionRequestBuilder {

    private OpenAIModel model;
    private Integer n = 1;
    @JsonProperty("max_tokens")
    private Integer maxTokens = 256;
    @JsonProperty("frequency_penalty")
    private Double frequencyPenalty = 0.0;
    @JsonProperty("presence_penalty")
    private Double presencePenalty = 0.0;
    private Double temperature = 1.0;
    @JsonProperty("top_p")
    private Double topP = 1.0;
    private String[] stop;
    private Boolean stream = false;
    private String userMessage;
    private String systemMessage;

    public ChatCompletionRequestBuilder model(OpenAIModel model) {
        this.model = model;
        return this;
    }

    public ChatCompletionRequestBuilder n(Integer n) {
        this.n = n;
        return this;
    }

    public ChatCompletionRequestBuilder maxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
        return this;
    }

    public ChatCompletionRequestBuilder frequencyPenalty(Double frequencyPenalty) {
        this.frequencyPenalty = frequencyPenalty;
        return this;
    }

    public ChatCompletionRequestBuilder presencePenalty(Double presencePenalty) {
        this.presencePenalty = presencePenalty;
        return this;
    }

    public ChatCompletionRequestBuilder temperature(Double temperature) {
        this.temperature = temperature;
        return this;
    }

    public ChatCompletionRequestBuilder topP(Double topP) {
        this.topP = topP;
        return this;
    }

    public ChatCompletionRequestBuilder stop(String[] stop) {
        this.stop = stop;
        return this;
    }

    public ChatCompletionRequestBuilder stream(Boolean stream) {
        this.stream = stream;
        return this;
    }

    public ChatCompletionRequestBuilder userMessage(String userMessage) {
        this.userMessage = userMessage;
        return this;
    }

    public ChatCompletionRequestBuilder systemMessage(String systemMessage) {
        this.systemMessage = systemMessage;
        return this;
    }

    public ChatCompletionRequest build() {
        validateRequiredFields();

        return new ChatCompletionRequest(
                this.model.getName(),
                this.n,
                this.maxTokens,
                this.frequencyPenalty,
                this.presencePenalty,
                this.temperature,
                this.topP,
                this.stop,
                this.stream,
                createOpenAIMessages()
        );
    }

    private void validateRequiredFields() {
        validateModel();
        validateMessages();
    }

    private void validateModel() {
        if (this.model == null) {
            throw new IllegalArgumentException("Model is required!");
        }
    }

    private void validateMessages() {
        var isUserMessageProvided = isUserMessageProvided();
        var isSystemMessageProvided = isSystemMessageProvided();

        if (!isUserMessageProvided && !isSystemMessageProvided) {
            throw new IllegalArgumentException("At least 1 message is required!");
        }
    }

    private boolean isUserMessageProvided() {
        return this.userMessage != null && !this.userMessage.isBlank();
    }

    private boolean isSystemMessageProvided() {
        return this.systemMessage != null && !this.systemMessage.isBlank();
    }

    private List<OpenAIMessage> createOpenAIMessages() {
        var messages = new ArrayList<OpenAIMessage>();

        if (isUserMessageProvided()) {
            messages.add(new OpenAIMessage("user", this.userMessage));
        }

        if (isSystemMessageProvided()) {
            messages.add(new OpenAIMessage("system", this.systemMessage));
        }

        return messages;
    }

}
