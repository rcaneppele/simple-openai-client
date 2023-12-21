package br.com.rcaneppele.openai.error;

public class APIErrorResponse {

    private APIError error;

    public APIError getError() {
        return error;
    }

    public void setError(APIError error) {
        this.error = error;
    }

}
