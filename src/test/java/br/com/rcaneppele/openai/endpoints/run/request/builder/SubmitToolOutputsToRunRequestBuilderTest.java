package br.com.rcaneppele.openai.endpoints.run.request.builder;

import br.com.rcaneppele.openai.common.validation.IdValidator;
import br.com.rcaneppele.openai.endpoints.run.request.SubmitToolOutputsToRunRequest;
import br.com.rcaneppele.openai.endpoints.run.request.ToolOutput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SubmitToolOutputsToRunRequestBuilderTest {

    @InjectMocks
    private SubmitToolOutputsToRunRequestBuilder builder;

    @Mock
    private IdValidator idValidator;

    @Test
    void shouldCallThreadIdValidator() {
        var threadId = "thread-id";
        builder.threadId(threadId);
        verify(idValidator).validateThreadId(threadId);
    }

    @Test
    void shouldCallRunIdValidator() {
        var runId = "run-id";
        builder.runId(runId);
        verify(idValidator).validateRunId(runId);
    }

    @Test
    void shouldCallRunIdValidatorOnBuild() {
        builder.threadId("thread-id").toolOutput("tool-call-id", "output").build();
        verify(idValidator).validateRunId(null);
    }

    @Test
    void shouldCallThreadIdValidatorOnBuild() {
        builder.runId("run-id").toolOutput("tool-call-id", "output").build();
        verify(idValidator).validateThreadId(null);
    }

    @Test
    void shouldRejectNullToolCallId() {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.toolOutput(null, "output"));
        Assertions.assertEquals("Tool call id is required!", exception.getMessage());
    }

    @ParameterizedTest(name = "Testing empty Tool call id")
    @ValueSource(strings = {" ", ""})
    void shouldRejectEmptyToolCallId(String id) {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.toolOutput(id, "output"));
        Assertions.assertEquals("Tool call id is required!", exception.getMessage());
    }

    @Test
    void shouldRejectNullToolOutput() {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.toolOutput("tool-call-id", null));
        Assertions.assertEquals("Tool output is required!", exception.getMessage());
    }

    @ParameterizedTest(name = "Testing empty Tool output")
    @ValueSource(strings = {" ", ""})
    void shouldRejectEmptyToolOutput(String output) {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.toolOutput("too-call-id", output));
        Assertions.assertEquals("Tool output is required!", exception.getMessage());
    }

    @Test
    void shouldBuildWithAllParameters() {
        var actual = builder
                .threadId("thread-id")
                .runId("run-id")
                .toolOutput("tool-call-id", "output")
                .build();

        var expected = new SubmitToolOutputsToRunRequest(
                "thread-id",
                "run-id",
                List.of(
                        new ToolOutput("tool-call-id", "output")
                )
        );

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

}
