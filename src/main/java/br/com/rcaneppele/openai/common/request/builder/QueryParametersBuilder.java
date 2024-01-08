package br.com.rcaneppele.openai.common.request.builder;

import br.com.rcaneppele.openai.common.request.QueryParameters;

public class QueryParametersBuilder {

    protected int limit = 20;
    protected String order = "desc";
    protected String before;
    protected String after;

    public QueryParametersBuilder limit(int limit) {
        if (limit < 1 || limit > 100) {
            throw new IllegalArgumentException("Limit must be between 1 and 100!");
        }
        this.limit = limit;
        return this;
    }

    public QueryParametersBuilder ascOrder() {
        this.order = "asc";
        return this;
    }

    public QueryParametersBuilder after(String after) {
        this.after = after;
        return this;
    }

    public QueryParametersBuilder before(String before) {
        this.before = before;
        return this;
    }

    public QueryParameters build() {
        return new QueryParameters(
                this.limit,
                this.order,
                this.after,
                this.before
        );
    }

}
