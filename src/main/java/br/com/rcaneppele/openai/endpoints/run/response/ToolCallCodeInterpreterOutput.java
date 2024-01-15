package br.com.rcaneppele.openai.endpoints.run.response;

public record ToolCallCodeInterpreterOutput(
    String type,
    String logs,
    RunImage image
) {}
