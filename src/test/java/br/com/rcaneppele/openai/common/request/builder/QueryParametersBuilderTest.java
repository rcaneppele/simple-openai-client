package br.com.rcaneppele.openai.common.request.builder;

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
        var request = builder.build();

        assertNotNull(request);
        assertEquals(20, request.limit());
        assertEquals("desc", request.order());
        assertNull(request.after());
        assertNull(request.before());
    }

    @Test
    void shouldBuildWithAscOrder() {
        var request = builder
                .ascOrder()
                .build();

        assertNotNull(request);
        assertEquals("asc", request.order());
    }

    @Test
    void shouldBuildWithAllParameters() {
        var limit = 50;
        var after = "after-id";
        var before = "before-id";

        var request = builder
                .limit(limit)
                .after(after)
                .before(before)
                .ascOrder()
                .build();

        assertNotNull(request);
        assertEquals(limit, request.limit());
        assertEquals(after, request.after());
        assertEquals(before, request.before());
        assertEquals("asc", request.order());
    }

}