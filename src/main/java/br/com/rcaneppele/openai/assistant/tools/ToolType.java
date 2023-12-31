package br.com.rcaneppele.openai.assistant.tools;

public enum ToolType {

    RETRIEVAL("retrieval"),
    CODE_INTERPRETER("code_interpreter"),
    FUNCTION_CALL("function");

    private final String name;

    ToolType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
