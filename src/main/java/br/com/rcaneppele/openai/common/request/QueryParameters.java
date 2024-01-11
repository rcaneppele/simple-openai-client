package br.com.rcaneppele.openai.common.request;

public record QueryParameters(
        Integer limit,
        String order,
        String after,
        String before) {
}
