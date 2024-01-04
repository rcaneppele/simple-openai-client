package br.com.rcaneppele.openai.endpoints.assistant.request;

import br.com.rcaneppele.openai.endpoints.assistant.tools.Tool;

import java.util.Map;
import java.util.Set;

public interface AssistantRequest {

    String model();
    String name();
    String description();
    String instructions();
    Set<Tool> tools();
    Set<String> fileIds();
    Map<String, String> metadata();

}
