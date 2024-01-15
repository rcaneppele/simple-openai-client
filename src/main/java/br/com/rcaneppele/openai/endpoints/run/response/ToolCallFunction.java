package br.com.rcaneppele.openai.endpoints.run.response;

public record ToolCallFunction(
    String name,
    String arguments,
    String output
) {}
