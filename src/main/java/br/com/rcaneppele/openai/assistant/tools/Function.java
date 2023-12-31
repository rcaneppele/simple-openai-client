package br.com.rcaneppele.openai.assistant.tools;

import java.util.Map;

public record Function(String name, String description, Map<String, Object> parameters) {}
