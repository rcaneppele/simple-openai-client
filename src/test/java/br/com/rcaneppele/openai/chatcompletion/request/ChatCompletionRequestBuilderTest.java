package br.com.rcaneppele.openai.chatcompletion.request;

import br.com.rcaneppele.openai.common.OpenAIModel;
import br.com.rcaneppele.openai.common.message.ChatMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ChatCompletionRequestBuilderTest {

    private ChatCompletionRequestBuilder builder;

    @BeforeEach
    void beforeEach() {
        this. builder = new ChatCompletionRequestBuilder();
    }

    @Test
    void shouldValidateModelParameter() {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.model(null));
        assertEquals("Model cannot be null!", exception.getMessage());
    }

    @ParameterizedTest(name = "Testing n parameter with: {0}")
    @ValueSource(ints = {0, -1, -10})
    void shouldValidateNParameter(int n) {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.n(n));
        assertEquals("Parameter n must be greater than or equal to 1!", exception.getMessage());
    }

    @ParameterizedTest(name = "Testing maxTokens parameter with: {0}")
    @ValueSource(ints = {0, -1, -10})
    void shouldValidateMaxTokensParameter(int maxTokens) {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.maxTokens(maxTokens));
        assertEquals("Parameter maxTokens must be greater than or equal to 1!", exception.getMessage());
    }

    @ParameterizedTest(name = "Testing frequencyPenalty parameter with: {0}")
    @ValueSource(doubles = {-2.1, -10.0, 2.1, 10.0})
    void shouldValidateFrequencyPenaltyParameter(double frequencyPenalty) {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.frequencyPenalty(frequencyPenalty));
        assertEquals("Parameter frequencyPenalty must be between -2.0 and 2.0!", exception.getMessage());
    }

    @ParameterizedTest(name = "Testing presencePenalty parameter with: {0}")
    @ValueSource(doubles = {-2.1, -10.0, 2.1, 10.0})
    void shouldValidatePresencePenaltyParameter(double presencePenalty) {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.presencePenalty(presencePenalty));
        assertEquals("Parameter presencePenalty must be between -2.0 and 2.0!", exception.getMessage());
    }

    @ParameterizedTest(name = "Testing temperature parameter with: {0}")
    @ValueSource(doubles = {-0.1, -10.0, 2.1, 10.0})
    void shouldValidateTemperatureParameter(double temperature) {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.temperature(temperature));
        assertEquals("Parameter temperature must be between 0 and 2.0!", exception.getMessage());
    }

    @ParameterizedTest(name = "Testing temperature parameter with: {0}")
    @ValueSource(doubles = {-0.1, -10.0, 2.1, 10.0})
    void shouldValidateTopPParameter() {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.topP(-0.1));
        assertEquals("Parameter topP must be between 0 and 1.0!", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> builder.topP(1.1));
        assertEquals("Parameter topP must be between 0 and 1.0!", exception.getMessage());
    }

    @Test
    void shouldValidateStopParameter() {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.stop("one", "two", "three", "four", "five"));
        assertEquals("Parameter stop must be up to 4 sequences!", exception.getMessage());
    }

    @ParameterizedTest(name = "Testing Logprobs parameter with: {0}")
    @ValueSource(ints = {-1, -10, 6, 10})
    void shouldValidateTopLogprobsParameter(int Logprobs) {
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.topLogprobs(Logprobs));
        assertEquals("Parameter topLogprobs must be between 0 and 5!", exception.getMessage());
    }

    @Test
    void shouldValidateLogitBiasParameter() {
        var invalid = Map.of(1, -101, 2, -110, 3, 101, 4, 110);
        var exception = assertThrows(IllegalArgumentException.class, () -> builder.logitBias(invalid));
        assertEquals("Values of logitBias parameter must be between -100 and 100!", exception.getMessage());
    }

    @Test
    void shouldNotBuildWithoutModel() {
        builder.userMessage("user message").systemMessage("system message");
        var exception = assertThrows(IllegalArgumentException.class, builder::build);
        assertEquals("Model is required!", exception.getMessage());
    }

    @Test
    void shouldNotBuildWithoutMessages() {
        builder.model(OpenAIModel.GPT_4_1106_PREVIEW);
        var exception = assertThrows(IllegalArgumentException.class, builder::build);
        assertEquals("At least 1 message is required!", exception.getMessage());
    }

    @Test
    void shouldBuildWithMultipleMessages() {
        builder.model(OpenAIModel.GPT_4_1106_PREVIEW).userMessage("user message 1").userMessage("user message 2").systemMessage("system message 1").systemMessage("system message 2");
        var request = builder.build();
        var messages = request.messages();
        assertEquals(4, messages.size());
        assertEquals(2, messages.stream().filter(m -> m.role().equals("user")).collect(Collectors.toList()).size());
        assertEquals(2, messages.stream().filter(m -> m.role().equals("system")).collect(Collectors.toList()).size());
    }

    @Test
    void shouldBuildWithAllParameters() {
        var userMessage = "the user message";
        var systemMessage = "the system message";
        String[] stopSequences = {"stop 1", "stop 2", "stop 3", "stop 4"};
        var logitBias = Map.of(100, 30, 200, 40, 300, 50);
        var user = "user-12345";

        var request = builder
                .model(OpenAIModel.GPT_3_5_TURBO)
                .n(3)
                .maxTokens(999)
                .frequencyPenalty(1.3)
                .presencePenalty(1.2)
                .temperature(1.1)
                .topP(0.7)
                .stop(stopSequences)
                .userMessage(userMessage)
                .systemMessage(systemMessage)
                .topLogprobs(4)
                .user(user)
                .seed(5)
                .logitBias(logitBias)
                .build();

        assertEquals(OpenAIModel.GPT_3_5_TURBO.getName(), request.model());
        assertEquals(3, request.n());
        assertEquals(999, request.maxTokens());
        assertEquals(1.3, request.frequencyPenalty());
        assertEquals(1.2, request.presencePenalty());
        assertEquals(1.1, request.temperature());
        assertEquals(0.7, request.topP());
        assertEquals(5, request.seed());
        assertEquals(user, request.user());

        assertEquals(4, request.topLogprobs());
        assertTrue(request.logprobs());

        assertEquals(4, request.stop().length);
        assertEquals(stopSequences, request.stop());

        assertEquals(2, request.messages().size());
        assertTrue(request.messages().contains(new ChatMessage("user", userMessage)));
        assertTrue(request.messages().contains(new ChatMessage("system", systemMessage)));

        assertEquals(logitBias, request.logitBias());
    }

}