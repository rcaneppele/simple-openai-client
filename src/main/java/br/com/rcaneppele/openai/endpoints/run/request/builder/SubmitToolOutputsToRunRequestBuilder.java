package br.com.rcaneppele.openai.endpoints.run.request.builder;

import br.com.rcaneppele.openai.common.validation.IdValidator;
import br.com.rcaneppele.openai.endpoints.run.request.SubmitToolOutputsToRunRequest;
import br.com.rcaneppele.openai.endpoints.run.request.ToolOutput;

import java.util.ArrayList;
import java.util.List;

public class SubmitToolOutputsToRunRequestBuilder {

    private String threadId;
    private String runId;
    private List<ToolOutput> toolOutputs = new ArrayList<>();

    private IdValidator idValidator = new IdValidator();

    public SubmitToolOutputsToRunRequestBuilder threadId(String threadId) {
        this.validateThreadId(threadId);
        this.threadId = threadId;
        return this;
    }

    public SubmitToolOutputsToRunRequestBuilder runId(String runId) {
        this.validateRunId(runId);
        this.runId = runId;
        return this;
    }

    public SubmitToolOutputsToRunRequestBuilder toolOutput(String toolCallId, String output) {
        if (toolCallId == null || toolCallId.isBlank()) {
            throw new IllegalArgumentException("Tool call id is required!");
        }

        if (output == null || output.isBlank()) {
            throw new IllegalArgumentException("Tool output is required!");
        }

        this.toolOutputs.add(new ToolOutput(toolCallId, output));
        return this;
    }

    public SubmitToolOutputsToRunRequest build() {
        validateRequiredFields();

        return new SubmitToolOutputsToRunRequest(
                this.threadId,
                this.runId,
                this.toolOutputs
        );
    }

    private void validateRequiredFields() {
        validateThreadId(this.threadId);
        validateRunId(this.runId);
        validateToolOutputs();
    }

    private void validateThreadId(String threadId) {
        this.idValidator.validateThreadId(threadId);
    }

    private void validateRunId(String runId) {
        this.idValidator.validateRunId(runId);
    }

    private void validateToolOutputs() {
        if (this.toolOutputs.isEmpty()) {
            throw new IllegalArgumentException("At least one tool output is required!");
        }
    }

}
