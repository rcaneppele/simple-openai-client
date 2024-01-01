package br.com.rcaneppele.openai.endpoints.assistant.tools;

public record Tool(String type, Function function) {}
