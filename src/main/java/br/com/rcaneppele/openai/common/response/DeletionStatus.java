package br.com.rcaneppele.openai.common.response;

public record DeletionStatus(
        String id,
        String object,
        boolean deleted
) {}
