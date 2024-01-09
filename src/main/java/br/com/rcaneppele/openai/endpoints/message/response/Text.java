package br.com.rcaneppele.openai.endpoints.message.response;

import java.util.List;

public record Text(
        String value,
        List<Annotation> annotations
) {}
