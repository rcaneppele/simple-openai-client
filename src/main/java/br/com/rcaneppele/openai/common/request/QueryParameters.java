package br.com.rcaneppele.openai.common.request;

public record QueryParameters(
        int limit,
        String order,
        String after,
        String before) {
}
