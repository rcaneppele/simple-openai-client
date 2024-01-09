package br.com.rcaneppele.openai.common.request.builder;

import br.com.rcaneppele.openai.common.request.QueryParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class QueryParametersBuilderTest {

    private QueryParametersBuilder builder;

    @BeforeEach
    void beforeEach() {
        this. builder = new QueryParametersBuilder();
    }

    @ParameterizedTest
    @ValueSource(ints = {-10, 0, 101, 110})
    void shouldValidateLimit(int limit) {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.limit(limit));
        assertEquals("Limit must be between 1 and 100!", exception.getMessage());
    }

    @Test
    void shouldBuildWithDefaultParameterValues() {
        var actual = builder.build();
        var expected = new QueryParameters(20, "desc", null, null);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void shouldBuildWithAscOrder() {
        var parameters = builder
                .ascOrder()
                .build();

        assertNotNull(parameters);
        assertEquals("asc", parameters.order());
    }

    @Test
    void shouldBuildWithAllParameters() {
        var limit = 50;
        var after = "after-id";
        var before = "before-id";

        var actual = builder
                .limit(limit)
                .after(after)
                .before(before)
                .ascOrder()
                .build();

        var expected = new QueryParameters(limit, "asc", after, before);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

}
