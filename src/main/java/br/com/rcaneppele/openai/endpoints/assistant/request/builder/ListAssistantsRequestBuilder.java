package br.com.rcaneppele.openai.endpoints.assistant.request.builder;

import br.com.rcaneppele.openai.endpoints.assistant.request.ListAssistantsRequest;

public class ListAssistantsRequestBuilder {

    private int limit = 20;
    private String order = "desc";
    private String before;
    private String after;

    public ListAssistantsRequestBuilder limit(int limit) {
        if (limit < 1 || limit > 100) {
            throw new IllegalArgumentException("Limit must be between 1 and 100!");
        }
        this.limit = limit;
        return this;
    }

    public ListAssistantsRequestBuilder ascOrder() {
        this.order = "asc";
        return this;
    }

    public ListAssistantsRequestBuilder after(String after) {
        this.after = after;
        return this;
    }

    public ListAssistantsRequestBuilder before(String before) {
        this.before = before;
        return this;
    }

    public ListAssistantsRequest build() {
        return new ListAssistantsRequest(
                this.limit,
                this.order,
                this.after,
                this.before
        );
    }

}
