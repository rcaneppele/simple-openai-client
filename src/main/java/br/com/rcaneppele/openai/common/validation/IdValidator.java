package br.com.rcaneppele.openai.common.validation;

public class IdValidator {

    public void validateAssistantId(String assistantId) {
        if (!isIdValid(assistantId)) {
            throw new IllegalArgumentException("Assistant id is required!");
        }
    }

    public void validateThreadId(String threadId) {
        if (!isIdValid(threadId)) {
            throw new IllegalArgumentException("Thread id is required!");
        }
    }

    public void validateFileId(String fileId) {
        if (!isIdValid(fileId)) {
            throw new IllegalArgumentException("File id is required!");
        }
    }

    public void validateMessageId(String messageId) {
        if (!isIdValid(messageId)) {
            throw new IllegalArgumentException("Message id is required!");
        }
    }

    public void validateRunId(String runId) {
        if (!isIdValid(runId)) {
            throw new IllegalArgumentException("Run id is required!");
        }
    }

    private boolean isIdValid(String id) {
        return id != null && !id.isBlank();
    }

}
