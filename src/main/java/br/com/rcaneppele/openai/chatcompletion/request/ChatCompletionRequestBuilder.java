package br.com.rcaneppele.openai.chatcompletion.request;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.common.message.ChatMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChatCompletionRequestBuilder {

    private OpenAIModel model;
    private Integer n = 1;
    private Integer maxTokens = 256;
    private Double frequencyPenalty = 0.0;
    private Double presencePenalty = 0.0;
    private Double temperature = 1.0;
    private Double topP = 1.0;
    private String[] stop;
    private String userMessage;
    private String systemMessage;
    private Boolean logprobs = false;
    private Integer topLogprobs;
    private String user;
    private Integer seed;
    private Map<Integer, Integer> logitBias;

    public ChatCompletionRequestBuilder model(OpenAIModel model) {
        if (model == null) {
            throw new IllegalArgumentException("Model cannot be null!");
        }
        this.model = model;
        return this;
    }

    public ChatCompletionRequestBuilder n(Integer n) {
        if (n == null || n < 1) {
            throw new IllegalArgumentException("Parameter n must be greater than or equal to 1!");
        }
        this.n = n;
        return this;
    }

    public ChatCompletionRequestBuilder maxTokens(Integer maxTokens) {
        if (maxTokens == null || maxTokens < 1) {
            throw new IllegalArgumentException("Parameter maxTokens must be greater than or equal to 1!");
        }
        this.maxTokens = maxTokens;
        return this;
    }

    public ChatCompletionRequestBuilder frequencyPenalty(Double frequencyPenalty) {
        if (frequencyPenalty == null || frequencyPenalty < -2.0 || frequencyPenalty > 2.0) {
            throw new IllegalArgumentException("Parameter frequencyPenalty must be between -2.0 and 2.0!");
        }
        this.frequencyPenalty = frequencyPenalty;
        return this;
    }

    public ChatCompletionRequestBuilder presencePenalty(Double presencePenalty) {
        if (presencePenalty == null || presencePenalty < -2.0 || presencePenalty > 2.0) {
            throw new IllegalArgumentException("Parameter presencePenalty must be between -2.0 and 2.0!");
        }
        this.presencePenalty = presencePenalty;
        return this;
    }

    public ChatCompletionRequestBuilder temperature(Double temperature) {
        if (temperature == null || temperature < 0 || temperature > 2.0) {
            throw new IllegalArgumentException("Parameter temperature must be between 0 and 2.0!");
        }
        this.temperature = temperature;
        return this;
    }

    public ChatCompletionRequestBuilder topP(Double topP) {
        if (topP == null || topP < 0 || topP > 1.0) {
            throw new IllegalArgumentException("Parameter topP must be between 0 and 1.0!");
        }
        this.topP = topP;
        return this;
    }

    public ChatCompletionRequestBuilder stop(String... stop) {
        if (stop == null || stop.length > 4) {
            throw new IllegalArgumentException("Parameter stop must be up to 4 sequences!");
        }
        this.stop = stop;
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

    public ChatCompletionRequestBuilder logprobs() {
        this.logprobs = true;
        return this;
    }

    public ChatCompletionRequestBuilder topLogprobs(Integer topLogprobs) {
        if (topLogprobs == null || topLogprobs < 0 || topLogprobs > 5) {
            throw new IllegalArgumentException("Parameter topLogprobs must be between 0 and 5!");
        }
        this.topLogprobs = topLogprobs;
        // To use the 'topLogprobs' parameter, 'logprobs' must be set to 'true'
        this.logprobs = true;
        return this;
    }

    public ChatCompletionRequestBuilder user(String user) {
        this.user = user;
        return this;
    }

    public ChatCompletionRequestBuilder seed(Integer seed) {
        this.seed = seed;
        return this;
    }

    public ChatCompletionRequestBuilder logitBias(Map<Integer, Integer> logitBias) {
        var valuesValid = logitBias.values().stream().allMatch(value -> value >= -100 && value <= 100);
        if (!valuesValid) {
            throw new IllegalArgumentException("Values of logitBias parameter must be between -100 and 100!");
        }
        this.logitBias = logitBias;
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
                createOpenAIMessages(),
                this.logprobs,
                this.topLogprobs,
                this.user,
                this.seed,
                this.logitBias
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

    private List<ChatMessage> createOpenAIMessages() {
        var messages = new ArrayList<ChatMessage>();

        if (isUserMessageProvided()) {
            messages.add(new ChatMessage("user", this.userMessage));
        }

        if (isSystemMessageProvided()) {
            messages.add(new ChatMessage("system", this.systemMessage));
        }

        return messages;
    }

}
