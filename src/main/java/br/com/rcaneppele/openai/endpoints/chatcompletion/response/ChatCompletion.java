package br.com.rcaneppele.openai.endpoints.chatcompletion.response;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.common.message.ChatCompletionChoice;
import br.com.rcaneppele.openai.common.message.TokenUsage;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

public record ChatCompletion(
        String id,
        String object,
        Instant created,
        OpenAIModel model,
        @JsonProperty("system_fingerprint")
        String systemFingerprint,
        List<ChatCompletionChoice> choices,
        TokenUsage usage
) {
    public String firstChoiceMessageContent() {
        return getMessageContent(0);
    }

    public String lastChoiceMessageContent() {
        if (this.choices == null || this.choices.isEmpty()) {
            return null;
        }

        return getMessageContent(this.choices.size() - 1);
    }

    private String getMessageContent(int choice) {
        if (this.choices == null || this.choices.isEmpty()) {
            return null;
        }

        return this.choices.get(choice).message().content();
    }

}
