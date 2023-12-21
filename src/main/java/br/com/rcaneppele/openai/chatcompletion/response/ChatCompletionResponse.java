package br.com.rcaneppele.openai.chatcompletion.response;

import br.com.rcaneppele.openai.common.TokenUsage;
import br.com.rcaneppele.openai.common.message.ChatCompletionChoice;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ChatCompletionResponse(
        String id,
        String object,
        Long created,
        String model,
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

    public String messageContentOfChoice(int choice) {
        return getMessageContent(choice);
    }

    private String getMessageContent(int choice) {
        if (this.choices == null || this.choices.isEmpty()) {
            return null;
        }

        return this.choices.get(choice).message().content();
    }

}
