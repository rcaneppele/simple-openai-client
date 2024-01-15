package br.com.rcaneppele.openai.endpoints.run.response;

import java.util.List;

public record ToolCallCodeInterpreter(
    String input,
    List<ToolCallCodeInterpreterOutput> outputs
) {}
