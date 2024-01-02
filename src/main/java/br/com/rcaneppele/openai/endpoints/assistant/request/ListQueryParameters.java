package br.com.rcaneppele.openai.endpoints.assistant.request;

public interface ListQueryParameters {

    int limit();
    String order();
    String after();
    String before();

}
