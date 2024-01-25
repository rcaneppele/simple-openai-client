package br.com.rcaneppele.openai.endpoints.run.request.builder;

import br.com.rcaneppele.openai.endpoints.run.request.SubmitToolOutputsToRunRequest;
import br.com.rcaneppele.openai.endpoints.run.request.ToolOutput;

import java.util.ArrayList;
import java.util.List;

public class SubmitToolOutputsToRunRequestBuilder {

    private List<ToolOutput> toolOutputs = new ArrayList<>();

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
                this.toolOutputs
        );
    }

    private void validateRequiredFields() {
        validateToolOutputs();
    }

    private void validateToolOutputs() {
        if (this.toolOutputs.isEmpty()) {
            throw new IllegalArgumentException("At least one tool output is required!");
        }
    }

}
